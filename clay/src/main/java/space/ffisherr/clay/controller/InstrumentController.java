package space.ffisherr.clay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.History;
import space.ffisherr.clay.entity.WantedInstruments;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.PlotXY;
import space.ffisherr.clay.service.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
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

    @GetMapping("/history/read-by-name/{name}")
    public  List<PlotXY> findByNameHistory(@PathVariable String name){
        return historyService.readByName(name);
    }

    @PostMapping("/add-instrument/")
    public void addWantedInstrument(@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        instrumentService.addWantedInstrument(name);
    }

    @PostMapping("/add-instruments/")
    public void addInstruments(@RequestBody String instruments) {
        Arrays.stream(instruments.split(",")).forEach(this::addWantedInstrument);
    }

    @PostMapping("/start-trading/")
    public void startTrading(@RequestParam String startTime,
                             @RequestParam String endTime) {
        tradeBot.doWork(startTime, endTime);
    }

    @GetMapping("/read-select/")
    public List<History> selectName(@RequestParam String param){
        return historyService.readHistoryByName(param);
    }

    @GetMapping("/load-csv/")
    public ResponseEntity<InputStreamResource> download(@RequestParam String param) throws IOException {
        File f = null;
        try {
            ExportCsv.main(historyService.readHistoryByName(param));
        } catch (Exception e) {
        e.printStackTrace();
    }
        f = new File("data.csv");
        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(param + ".csv").build();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(f));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .contentLength(f.length()).headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
