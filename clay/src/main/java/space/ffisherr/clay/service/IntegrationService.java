package space.ffisherr.clay.service;

import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.TransactionRequestDTO;
import space.ffisherr.clay.model.TransactionResponseDTO;

import java.util.List;

public interface IntegrationService {

    List<ClayInstrument> readAllInstruments();

    ClayInstrument readTicker(String tikerName, String tikerVal);

    TransactionResponseDTO sendSell(TransactionRequestDTO request);

    TransactionResponseDTO sendBuy(TransactionRequestDTO request);
}
