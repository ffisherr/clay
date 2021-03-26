package space.ffisherr.clayapi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.ffisherr.clayapi.entity.Ticker;
import space.ffisherr.clayapi.model.TransactionRequestDTO;
import space.ffisherr.clayapi.model.TransactionResponseDTO;
import space.ffisherr.clayapi.repository.InstrumentRepository;
import space.ffisherr.clayapi.service.TransactionServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/ticker/")
@RequiredArgsConstructor
public class TickerController {

    private final InstrumentRepository repository;
    private final TransactionServiceImpl service;

    @GetMapping
    public ResponseEntity<Ticker> readAllByTicker(@RequestParam String tickerName, @RequestParam String timeVal) {
        final Optional<Ticker> optionalTicker = repository.findByTicker(tickerName, timeVal);
        return optionalTicker.map(ticker -> new ResponseEntity<>(ticker, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/unique/")
    public List<String> readUniqueByTicker() {
        return repository.findUniqueTicker();
    }

    @PostMapping("/sell/")
    public TransactionResponseDTO tickerSellTransaction(@RequestBody TransactionRequestDTO requestDTO){
        return service.userSellTicker(requestDTO);
    }

    @PostMapping("/buy/")
    public TransactionResponseDTO tickerBuyTransaction(@RequestBody TransactionRequestDTO requestDTO){
        return service.userBuyTicker(requestDTO);
    }


}
