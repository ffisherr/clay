package space.ffisherr.clay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.History;
import space.ffisherr.clay.repository.HistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository repository;

    @Override
    public List<History> readAll() {
        return (List<History>)repository.findAll();
    }

    @Override
    public History create(String direction, Bag currentBag, Long totalAmount,
                          Long oneItemCost, Long purchasedNumber, Long step) {
        final History history = new History();
        history.setDirection(direction);
        history.setTotalAmount(totalAmount);
        history.setPurchasedNumber(purchasedNumber);
        history.setInstrument(currentBag.getWantedInstrument());
        history.setOneItemCost(oneItemCost);
        history.setLeftAmount(currentBag.getAmount());
        history.setCreatedAt(step);
        return repository.save(history);
    }
}
