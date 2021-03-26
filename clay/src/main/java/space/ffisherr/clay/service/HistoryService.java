package space.ffisherr.clay.service;

import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.History;

import java.util.List;

public interface HistoryService {

    List<History> readAll();

    History create(String direction, Bag currentBag,
                   Long totalAmount, Long oneItemCost,
                   Long purchasedNumber);

}
