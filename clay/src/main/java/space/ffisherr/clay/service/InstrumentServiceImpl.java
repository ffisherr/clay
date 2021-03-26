package space.ffisherr.clay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.ffisherr.clay.entity.WantedInstruments;
import space.ffisherr.clay.repository.WantedInstrumentRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final WantedInstrumentRepository repository;
    private final BagService bagService;

    @Override
    public List<WantedInstruments> readAll() {
        return (List<WantedInstruments>) repository.findAll();
    }

    @Override
    public void addWantedInstrument(String name) {
        if (repository.findByName(name).isPresent()) {
            log.error("Не найден инструмент: {}", name);
            return;
        }
        WantedInstruments instrument = new WantedInstruments();
        instrument.setName(name);
        instrument = repository.save(instrument);
        bagService.create(instrument);
    }
}
