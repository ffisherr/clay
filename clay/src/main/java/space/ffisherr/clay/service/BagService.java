package space.ffisherr.clay.service;

import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.WantedInstruments;

import java.util.List;

public interface BagService {

    List<Bag> readAll();

    void create(WantedInstruments instrument);

}
