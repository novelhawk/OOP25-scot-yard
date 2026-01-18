package it.unibo.scotyard.view.tracker;

import it.unibo.scotyard.model.map.TransportType;
import java.util.List;

public interface TrackerPanel {
    void createGrid(int count);

    void setTransportModes(int index, List<TransportType> transportTypes);
}
