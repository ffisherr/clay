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
    private String  time;
    private String open;
    private String  hight;
    private String  low;
    private String  close;
    private String  vol;
}
