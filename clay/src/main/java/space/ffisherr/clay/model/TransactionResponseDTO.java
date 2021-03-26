package space.ffisherr.clay.model;

import lombok.Data;

@Data
public class TransactionResponseDTO {

    private Integer boughtTicker;
    private Integer leftMoney;
    private  Float costOneTicker;
}
