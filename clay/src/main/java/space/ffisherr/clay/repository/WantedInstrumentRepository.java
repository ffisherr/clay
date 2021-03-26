package space.ffisherr.clay.repository;

import org.springframework.data.repository.CrudRepository;
import space.ffisherr.clay.entity.WantedInstruments;

import java.util.Optional;

public interface WantedInstrumentRepository extends CrudRepository<WantedInstruments, Long> {

    Optional<WantedInstruments> findByName(String name);
}
