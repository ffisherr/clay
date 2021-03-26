package space.ffisherr.clay.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.RsiModel;
import space.ffisherr.clay.model.TransactionRequestDTO;
import space.ffisherr.clay.model.TransactionResponseDTO;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TradeBot {

    private final BagService bagService;
    private final IntegrationService integrationService;
    private final InstrumentService instrumentService;
    private final HistoryService historyService;
    private final RsiCounterService rsiCounterService = new RsiCounterService();

    public void doWork(String startedAt, String endedAt) {
        final int startTimeInt = Integer.parseInt(convertToString(startedAt));
        final int endTimeInt = Integer.parseInt(convertToString(endedAt));
        final List<Bag> bagList = bagService.readAll();
        if (bagList.isEmpty()) {
            log.error("Не найдено инструментов для торгов");
        }
        log.info("Начало работы: {} Окончание работы: {}", startTimeInt, endTimeInt);
        rsiCounterService.initialize(bagList, startTimeInt);
        for (int i = startTimeInt; i < endTimeInt; i++) {
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
        final RsiModel model = rsiCounterService.getRsi(clay, step);
        if (model.getPrevious() > 30 && model.getCurrent() <= 30) {
            buyAll(bag, step);
        } else if (model.getPrevious() < 70 && model.getCurrent() >= 70) {
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
        bag.setInstrumentAmount(bag.getInstrumentAmount() + response.getBoughtTicker());
        bag.setAmount(response.getLeftMoney().longValue());
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
