package space.ffisherr.clay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.History;
import space.ffisherr.clay.entity.WantedInstruments;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.service.BagService;
import space.ffisherr.clay.service.HistoryService;
import space.ffisherr.clay.service.InstrumentService;
import space.ffisherr.clay.service.IntegrationService;
import space.ffisherr.clay.service.TradeBot;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/instruments/")
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;
    private final IntegrationService integrationService;
    private final HistoryService historyService;
    private final BagService bagService;
    private final TradeBot tradeBot;

    @GetMapping("/clay/read-all/")
    public List<ClayInstrument> readAllClayInstruments() {
        return integrationService.readAllInstruments();
    }

    @GetMapping("/history/read-all/")
    public List<History> readAllHistory() {
        return historyService.readAll();
    }

    @GetMapping("/my/instruments/")
    public List<WantedInstruments> readAllWanted() {
        return instrumentService.readAll();
    }

    @GetMapping("/bag/read-all/")
    public List<Bag> readAllBags() {
        return bagService.readAll();
    }

    @PostMapping("/add-instrument/")
    public void addWantedInstrument(@RequestParam String name) {
        instrumentService.addWantedInstrument(name);
    }

    @PostMapping("/start-trading/")
    public void startTrading(@RequestParam Timestamp startTime,
                             @RequestParam Timestamp endTime) {
        tradeBot.doWork(startTime, endTime);
    }

}
