package space.ffisherr.clay.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import space.ffisherr.clay.entity.RsiData;

import java.util.List;
import java.util.Optional;

public interface RsiDataRepository extends CrudRepository<RsiData, Long> {

    @Query("select rsi from RsiData rsi where rsi.instrumentName = :ticker order by rsi.id desc")
    List<RsiData> findAllByName(@Param("ticker") String ticker);

    @Query("select sum(r.u)/count(r) from RsiData r where r.instrumentName = :ticker")
    float sumU(@Param("ticker") String ticker);

    @Query("select sum(r.d)/count(r) from RsiData r where r.instrumentName = :ticker")
    float sumD(@Param("ticker") String ticker);
}
