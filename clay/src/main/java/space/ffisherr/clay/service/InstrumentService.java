package space.ffisherr.clay.service;

import space.ffisherr.clay.entity.WantedInstruments;

import java.util.List;

public interface InstrumentService {

    List<WantedInstruments> readAll();

    void addWantedInstrument(String instrument);

}
