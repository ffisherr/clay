package space.ffisherr.clay.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class RsiData {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_r")
    @SequenceGenerator(name = "id_seq_r", sequenceName = "rsi_data_seq_id")
    private Long id;

    private Float u;
    private Float d;
    private Float uS;
    private Float dS;
    private Float current;
    private Float previous;
    private String instrumentName;
    private Integer step;
    private Float rsi;

}
