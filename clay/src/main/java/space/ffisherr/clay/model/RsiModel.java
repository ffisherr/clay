package space.ffisherr.clay.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RsiModel {

    private Float previous;
    private Float current;

}
