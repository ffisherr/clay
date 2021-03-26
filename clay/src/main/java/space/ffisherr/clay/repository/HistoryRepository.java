package space.ffisherr.clay.repository;

import org.springframework.data.repository.CrudRepository;
import space.ffisherr.clay.entity.History;

public interface HistoryRepository extends CrudRepository<History, Long> {
}
