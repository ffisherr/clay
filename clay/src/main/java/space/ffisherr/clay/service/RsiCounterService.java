package space.ffisherr.clay.service;


import space.ffisherr.clay.entity.Bag;
import space.ffisherr.clay.entity.RsiData;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.RsiModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RsiCounterService {

    private Map<String, RsiModel> clayMap = new HashMap<>();
    private int startTimeInt;

    public void initialize(List<Bag> instruments, int startTimeInt) {
        this.startTimeInt = startTimeInt;
        for (Bag bag : instruments) {
            clayMap.put(bag.getWantedInstrument().getName(), new RsiModel(0f, 0f));
        }
    }

    public RsiModel getRsi(ClayInstrument instrument) {
        updateRsi( instrument);
        return clayMap.get(instrument.getTicker());
    }

    private void updateRsi(ClayInstrument instrument) {
        final RsiModel model = clayMap.get(instrument.getTicker());
        final Float currentValue = model.getCurrent();
        model.setPrevious(currentValue);
        model.setCurrent(Float.parseFloat(instrument.getClose()));
    }

}
