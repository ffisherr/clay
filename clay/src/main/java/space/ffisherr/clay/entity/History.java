package space.ffisherr.clay.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.sql.Timestamp;

@Entity
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_h")
    @SequenceGenerator(name = "id_seq_h", sequenceName = "history_seq_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "wanted_history_id")
    private WantedInstruments instrument;
    private String direction;
    private Long purchasedNumber;
    private Long totalAmount;
    private Long oneItemCost;
    private Long leftAmount;
    private Long createdAt;

}
