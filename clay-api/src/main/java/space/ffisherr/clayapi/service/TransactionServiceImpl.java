package space.ffisherr.clayapi.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import space.ffisherr.clayapi.entity.Ticker;
import space.ffisherr.clayapi.model.TransactionRequestDTO;
import space.ffisherr.clayapi.model.TransactionResponseDTO;
import space.ffisherr.clayapi.repository.InstrumentRepository;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final InstrumentRepository repository;

    @Override
    public TransactionResponseDTO userBuyTicker(TransactionRequestDTO requestDTO) {
         Ticker t = repository.findByTicker(requestDTO.getNameTicker(), requestDTO.getTime()).get();
        float f = Float.parseFloat(t.getClose());
//        System.out.println(f);
        double bougth = Math.floor(requestDTO.getAllMoney()/f);
        double left = requestDTO.getAllMoney() - (bougth*f);
//        System.out.println(String.format("%f %f %f", f, bougth, left));
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        int intleft = (int)left;
        int intbougth = (int)bougth;
        responseDTO.setLeftMoney(intleft);
        responseDTO.setBoughtTicker(intbougth);
        responseDTO.setCostOneTicker(f);
        return responseDTO;
    }

    @Override
    public TransactionResponseDTO userSellTicker(TransactionRequestDTO requestDTO) {
        Ticker t = repository.findByTicker(requestDTO.getNameTicker(), requestDTO.getTime()).get();
        float f = Float.parseFloat(t.getClose());
        int sold = (int) f * requestDTO.getInstrumentAmount();

        final TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setBoughtTicker(requestDTO.getInstrumentAmount());
        responseDTO.setCostOneTicker(f);
        responseDTO.setLeftMoney(requestDTO.getAllMoney() + sold);
        return responseDTO;
    }
}
