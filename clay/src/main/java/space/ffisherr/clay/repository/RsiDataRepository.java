package space.ffisherr.clay.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.ffisherr.clay.entity.RsiData;

import java.util.List;

public interface RsiDataRepository extends CrudRepository<RsiData, Long> {

    @Query("select rsi from RsiData rsi where rsi.wantedInstrument.name = :ticker order by id desc")
    List<RsiData> findAllByName(@Param("ticker") String ticker);
}
