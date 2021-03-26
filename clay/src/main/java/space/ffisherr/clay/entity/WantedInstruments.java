package space.ffisherr.clay.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Data
public class WantedInstruments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_w")
    @SequenceGenerator(name = "id_seq_w", sequenceName = "wanted_instruments_seq_id")
    private Long id;

    private String name;

}
