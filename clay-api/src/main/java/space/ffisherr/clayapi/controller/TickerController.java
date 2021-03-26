package space.ffisherr.clayapi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import space.ffisherr.clayapi.entity.Ticker;
import space.ffisherr.clayapi.repository.InstrumentRepository;

@Slf4j
@RestController
@RequestMapping("/ticker/")
@RequiredArgsConstructor
public class TickerController {

    private final InstrumentRepository repository;

    @GetMapping
    public Ticker readAllByTicker(@RequestParam String tickerName, String timeVal) {
        return repository.findByTicker(tickerName, timeVal).orElse(null);
    }

}
