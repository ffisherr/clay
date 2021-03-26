package space.ffisherr.clay.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.ffisherr.clay.entity.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends CrudRepository<History, Long> {

    @Query("select c from History c where c.instrument.name = :instrument")
    List<History> findByInstrument(@Param("instrument") String instrument);
}
