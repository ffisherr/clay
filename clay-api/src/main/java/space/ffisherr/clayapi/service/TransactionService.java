package space.ffisherr.clayapi.service;

import space.ffisherr.clayapi.model.TransactionRequestDTO;
import space.ffisherr.clayapi.model.TransactionResponseDTO;

public interface TransactionService {

    TransactionResponseDTO userSendMoney(TransactionRequestDTO requestDTO);


}
