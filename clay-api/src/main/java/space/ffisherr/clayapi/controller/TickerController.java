package space.ffisherr.clayapi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import space.ffisherr.clayapi.entity.Ticker;
import space.ffisherr.clayapi.model.TransactionRequestDTO;
import space.ffisherr.clayapi.model.TransactionResponseDTO;
import space.ffisherr.clayapi.repository.InstrumentRepository;
import space.ffisherr.clayapi.service.TransactionServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ticker/")
@RequiredArgsConstructor
public class TickerController {

    private final InstrumentRepository repository;
    private final TransactionServiceImpl service;

    @GetMapping
    public Ticker readAllByTicker(@RequestParam String tickerName, String timeVal) {
        return repository.findByTicker(tickerName, timeVal).orElse(null);
    }

    @GetMapping("/unique/")
    public List<String> readUniqueByTicker() {
        return repository.findUniqueTicker();
    }

    @PostMapping("/transaction/")
    public TransactionResponseDTO tickerTransaction(@RequestBody TransactionRequestDTO requestDTO){
        return service.userSendMoney(requestDTO);
    }


}
