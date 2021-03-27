package space.ffisherr.clay.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class RsiData {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_r")
    @SequenceGenerator(name = "id_seq_r", sequenceName = "rsi_data_seq_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wanted_instrument_id")
    private WantedInstruments wantedInstrument;
    private String u;
    private String d;

}
