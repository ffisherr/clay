package space.ffisherr.clayapi.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Data
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @SequenceGenerator(name = "id_seq", sequenceName = "ticker_seq_id")
    private Long id;
    private String ticker;
    private Integer time;
    private Long open;
    private Long hight;
    private Long low;
    private Long close;
    private Long vol;
}
