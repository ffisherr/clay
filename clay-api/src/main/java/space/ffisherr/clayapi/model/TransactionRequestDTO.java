package space.ffisherr.clayapi.model;

import lombok.Data;


@Data
public class TransactionRequestDTO {

    private String nameTicker;
    private Integer allMoney;
    private Integer instrumentAmount;
    private String time;

}
