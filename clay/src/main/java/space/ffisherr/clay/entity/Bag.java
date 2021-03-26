package space.ffisherr.clay.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Data
public class Bag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_b")
    @SequenceGenerator(name = "id_seq_b", sequenceName = "bag_seq_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wanted_instrument_id")
    private WantedInstruments wantedInstrument;
    private Long amount;
    private Long instrumentAmount;

}
