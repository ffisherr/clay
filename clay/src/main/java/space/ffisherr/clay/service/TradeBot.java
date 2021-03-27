package space.ffisherr.clay.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TradeBot {

    private final BagService bagService;
    private final IntegrationService integrationService;
    private final InstrumentService instrumentService;
    private final HistoryService historyService;
    private final RsiDataRepository rsiDataRepository;
    private final RsiCounterService rsiCounterService = new RsiCounterService();
    private Map<String, RsiModel> rsiMap = new HashMap<>();

    public void doWork(String startedAt, String endedAt) {
        final int startTimeInt = Integer.parseInt(convertToString(startedAt));
        final int endTimeInt = Integer.parseInt(convertToString(endedAt));
        final List<Bag> bagList = bagService.readAll();
        if (bagList.isEmpty()) {
            log.error("Не найдено инструментов для торгов");
        }
        log.info("Начало работы: {} Окончание работы: {}", startTimeInt, endTimeInt);
        for (Bag bag : bagList) {
            rsiMap.put(bag.getWantedInstrument().getName(), new RsiModel(0f, 0f));
        }
        rsiCounterService.initialize(bagList, startTimeInt);
        for (int i = startTimeInt; i < endTimeInt; i+=100) {
            for (Bag bag : bagList) {
                processTransaction(bag, i, startTimeInt);
            }
        }
    }

    protected void processTransaction(Bag bag, int step, int startTimeInt) {
        final ClayInstrument clay = integrationService.readTicker(bag.getWantedInstrument().getName(),
                String.valueOf(step));
        if (clay == null) {
//            log.warn("На шаге {} данные отсутствуют", step);
            return;
        }
        final RsiModel model = rsiCounterService.getRsi(clay);
        final List<RsiData> rsiData = rsiDataRepository.findAllByName(clay.getTicker());
        if (rsiData.size() < 14) {
            final RsiData r = new RsiData();
            if (rsiData.size() == 0) {
                r.setD("0");
                r.setU("0");
            } else {
                if (model.getCurrent() > model.getPrevious()) {
                    r.setD("0");
                    r.setU(String.valueOf(model.getCurrent() - model.getPrevious()));
                } else if (model.getCurrent().equals(model.getPrevious())) {
                    r.setD("0");
                    r.setU("0");
                } else {
                    r.setU("0");
                    r.setD(String.valueOf(model.getPrevious() - model.getCurrent()));
                }
            }
            r.setWantedInstrument(bag.getWantedInstrument());
            rsiDataRepository.save(r);
            return;
        }
        float meanU = 0f, meanD = 0f;
        for (RsiData rD : rsiData) {
            meanU += Float.parseFloat(rD.getU());
            meanD += Float.parseFloat(rD.getD());
        }
        RsiData r = new RsiData();
        if (model.getCurrent() > model.getPrevious()) {
            r.setD("0");
            r.setU(String.valueOf(model.getCurrent() - model.getPrevious()));
        } else if (model.getCurrent().equals(model.getPrevious())) {
            r.setD("0");
            r.setU("0");
        } else {
            r.setU("0");
            r.setD(String.valueOf(model.getPrevious() - model.getCurrent()));
        }
        r.setWantedInstrument(bag.getWantedInstrument());
        r = rsiDataRepository.save(r);
        meanU += Float.parseFloat(r.getU());
        meanD += Float.parseFloat(r.getD());
        meanU /= (rsiData.size() + 1);
        meanD /= (rsiData.size() + 1);
        float reallyFinalRs = meanU / meanD;
        float reallyFinalRsi = 100 - (100 / (1 + reallyFinalRs));
        log.error("Time={} RS={} RSI = {}", step, reallyFinalRs, reallyFinalRsi);
        final RsiModel rsiCondition = rsiMap.get(bag.getWantedInstrument().getName());
        rsiCondition.setPrevious(rsiCondition.getCurrent());
        rsiCondition.setCurrent(reallyFinalRsi);

        if (rsiCondition.getPrevious() > 40 && rsiCondition.getCurrent() <= 40) {
            buyAll(bag, step);
        } else if (rsiCondition.getPrevious() < 50 && rsiCondition.getCurrent() >= 50) {
            sellAll(bag, step);
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
