package space.ffisherr.clay.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.RsiData;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.RsiModel;
import space.ffisherr.clay.model.TransactionRequestDTO;
import space.ffisherr.clay.model.TransactionResponseDTO;
import space.ffisherr.clay.repository.RsiDataRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TradeBot {

    private static final float ALPHA = 0.68f;
    private final BagService bagService;
    private final IntegrationService integrationService;
    private final InstrumentService instrumentService;
    private final HistoryService historyService;
    private final RsiDataRepository rsiDataRepository;

    public void doWork(String startedAt, String endedAt) {
        final int startTimeInt = Integer.parseInt(convertToString(startedAt));
        final int endTimeInt = Integer.parseInt(convertToString(endedAt));
        final List<Bag> bagList = bagService.readAll();
        if (bagList.isEmpty()) {
            log.error("Не найдено инструментов для торгов");
        }
        log.info("Начало работы: {} Окончание работы: {}", startTimeInt, endTimeInt);
//        initialize(bagList, startTimeInt);
        for (int i = startTimeInt; i < endTimeInt; i+=100) {
            for (Bag bag : bagList) {
                processTransaction(bag, i, startTimeInt);
            }
        }
    }

//    private void initialize(List<Bag> bagList, int startTime) {
//        bagList.forEach(bag -> {
//            final ClayInstrument instrument = integrationService.readTicker(bag.getWantedInstrument().getName(),
//                    String.valueOf(startTime));
//            final RsiData entity = new RsiData();
//            entity.setU(0f);
//            entity.setD(0f);
//            entity.setRsi(0f);
//            entity.setCurrent(Float.parseFloat("0"));
//            entity.setPrevious(0f);
//            entity.setInstrumentName(bag.getWantedInstrument().getName());
//            entity.setStep(startTime);
//            rsiDataRepository.save(entity);
//        });
//    }

    protected void processTransaction(Bag bag, int step, int startTimeInt) {
        final ClayInstrument clay = integrationService.readTicker(bag.getWantedInstrument().getName(),
                String.valueOf(step));
        if (clay == null) {
//            log.warn("На шаге {} данные отсутствуют", step);
            return;
        }
        final String ticker = clay.getTicker();
        if (rsiDataRepository.findAllByName(ticker).isEmpty()) {
            final RsiData entity = new RsiData();
            entity.setU(0f);
            entity.setD(0f);
            entity.setRsi(0f);
            entity.setCurrent(Float.parseFloat("0"));
            entity.setPrevious(0f);
            entity.setDS(0f); entity.setUS(0f);
            entity.setInstrumentName(bag.getWantedInstrument().getName());
            entity.setStep(step);
            rsiDataRepository.save(entity);
            return;
        }
        final Optional<RsiData> optionalEntity = Optional.of(rsiDataRepository.findAllByName(ticker).get(0));
        if (optionalEntity.isPresent()) {
            final RsiData entity = optionalEntity.get();
            final float oldRsi = entity.getRsi();
            RsiData newEntity = new RsiData();
            float current = Float.parseFloat(clay.getClose());
            newEntity.setPrevious(entity.getCurrent());
            newEntity.setCurrent(current);
            newEntity.setStep(step);
            newEntity.setInstrumentName(entity.getInstrumentName());
            if ( current > entity.getCurrent()) {
                newEntity.setU(current - entity.getCurrent());
                newEntity.setD(0f);
            } else if (current < entity.getCurrent()) {
                newEntity.setU(0f);
                newEntity.setD(entity.getCurrent() - current);
            } else {
                newEntity.setU(0.0f);newEntity.setD(0.0f);
            }
            newEntity = rsiDataRepository.save(newEntity);

            float d_s = entity.getDS() + ALPHA * (newEntity.getD() - entity.getDS());
            float u_s = entity.getUS() + ALPHA * (newEntity.getU() - entity.getUS());
            newEntity.setDS(d_s);
            newEntity.setUS(u_s);
            float rsi = 100 - (100 / (1 + (u_s / d_s)));
            newEntity.setRsi(rsi);
            rsiDataRepository.save(newEntity);
            log.error("Time={} RSI={}", step, rsi);
            if (((List<RsiData>)rsiDataRepository.findAll()).size() > 14) {
                if (rsi >= 70 && oldRsi < 70) {
                    sellAll(bag, step);
                }
                if (rsi <= 30 && oldRsi > 30) {
                    buyAll(bag, step);
                }
            }
        }

    }

    private void sellAll(Bag bag, int step) {
        if (bag.getInstrumentAmount() == 0) {
            log.info("Нечего продавать");
            return;
        }
        log.info("Продаем: {} Время: {}", bag.getWantedInstrument().getName(), step);
        final TransactionRequestDTO request = new TransactionRequestDTO();
        request.setNameTicker(bag.getWantedInstrument().getName());
        request.setTime(String.valueOf(step));
        request.setAllMoney(bag.getAmount().intValue());
        request.setInstrumentAmount(bag.getInstrumentAmount().intValue());
        final TransactionResponseDTO response = integrationService.sendSell(request);
        bag.setAmount(response.getLeftMoney().longValue());
        bag.setInstrumentAmount(0L);
        bagService.save(bag);
        log.info("Остаток денег: {} По инструменту: {}", response.getLeftMoney(),
                bag.getWantedInstrument().getName());
        createHistory(response,  bag,"Продажа", (long) step);
    }

    private void buyAll(Bag bag, int step) {
        if (bag.getAmount() == 0) {
            log.info("Нет денег для покупки");
            return;
        }
        log.info("Покупаем: {} Время: {}", bag.getWantedInstrument().getName(), step);
        final TransactionRequestDTO request = new TransactionRequestDTO();
        request.setNameTicker(bag.getWantedInstrument().getName());
        request.setTime(String.valueOf(step));
        request.setAllMoney(bag.getAmount().intValue());
        final TransactionResponseDTO response = integrationService.sendBuy(request);
        if (response.getBoughtTicker() == 0) {
            log.warn("Недостаточно средств для покупки");
            return;
        }
        bag.setInstrumentAmount(bag.getInstrumentAmount() + response.getBoughtTicker());
        bag.setAmount(response.getLeftMoney().longValue());
        bagService.save(bag);
        log.info("Остаток денег: {} По инструменту: {}", response.getLeftMoney(),
                bag.getWantedInstrument().getName());
        log.info("Количество инструментов: {}", bag.getInstrumentAmount());
        createHistory(response, bag, "Покупка", (long) step);
    }

    private void createHistory(TransactionResponseDTO response, Bag bag, String direction, Long step) {
        historyService.create(direction, bag, bag.getInstrumentAmount(), response.getCostOneTicker().longValue(),
                response.getBoughtTicker().longValue(), step);
    }

    protected String convertToString(String timestamp) {
        final String[] arr = timestamp.split(":");
        return arr[0]+arr[1]+"00";
    }
}
