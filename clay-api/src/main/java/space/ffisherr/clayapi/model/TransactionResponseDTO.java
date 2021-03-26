package space.ffisherr.clayapi.model;

import lombok.Data;

@Data
public class TransactionResponseDTO {

    private Integer boughtTicker;
    private Integer leftMoney;
    private  Float costOneTicker;
}
