package space.ffisherr.clay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.WantedInstruments;
import space.ffisherr.clay.repository.BagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BagServiceImpl implements BagService {

    private final BagRepository repository;

    @Override
    public List<Bag> readAll() {
        return (List<Bag>) repository.findAll();
    }

    @Override
    public void create(WantedInstruments instrument) {
        final Bag bag = new Bag();
        bag.setWantedInstrument(instrument);
        bag.setAmount(1000000L);
        bag.setInstrumentAmount(0L);
        repository.save(bag);
    }

    @Override
    public Bag save(Bag bag) {
        return repository.save(bag);
    }

}
