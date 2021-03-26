package space.ffisherr.clayapi.service;

import space.ffisherr.clayapi.model.TransactionRequestDTO;
import space.ffisherr.clayapi.model.TransactionResponseDTO;

public interface TransactionService {

    TransactionResponseDTO userBuyTicker(TransactionRequestDTO requestDTO);


    TransactionResponseDTO userSellTicker(TransactionRequestDTO requestDTO);
}
