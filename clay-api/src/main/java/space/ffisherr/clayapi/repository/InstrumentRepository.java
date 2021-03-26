package space.ffisherr.clayapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.ffisherr.clayapi.entity.Ticker;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends CrudRepository<Ticker, Long> {

    @Query("select c from Ticker c where c.ticker = :tickerName and c.time = :timeVal")
    Optional<Ticker> findByTicker(@Param("tickerName") String tickerName, @Param("timeVal") String timeVal);

}
