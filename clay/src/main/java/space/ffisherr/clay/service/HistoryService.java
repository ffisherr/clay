package space.ffisherr.clay.service;

import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.History;
import space.ffisherr.clay.model.PlotXY;

import java.util.List;

public interface HistoryService {

    List<History> readAll();
    List<PlotXY> readByName(String name);

    History create(String direction, Bag currentBag,
                   Long totalAmount, Long oneItemCost,
                   Long purchasedNumber, Long step);



}
