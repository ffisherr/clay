package space.ffisherr.clay.model;

import lombok.Data;

@Data
public class ClayInstrument {

    private Long id;
    private String ticker;
    private String  time;
    private String open;
    private String  hight;
    private String  low;
    private String  close;
    private String  vol;
}
