package it.unibo.scotyard.view.resources;

import it.unibo.scotyard.model.map.TransportType;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;

public class IconRegistryImpl implements IconRegistry {
    private static final String ICON_PATH_PREFIX = "/it/unibo/scotyard/icons/";

    private static final Map<TransportType, String> TRASPORT_TYPE_PATHS = Map.of(
            TransportType.TAXI, ICON_PATH_PREFIX + "taxi.png",
            TransportType.UNDERGROUND, ICON_PATH_PREFIX + "subway.png",
            TransportType.FERRY, ICON_PATH_PREFIX + "boat.png",
            TransportType.BUS, ICON_PATH_PREFIX + "bus.png");

    private final Map<TransportType, ImageIcon> transportIcons = new EnumMap<>(TransportType.class);

    @Override
    public ImageIcon getTransportIcon(final TransportType transportType) {
        return transportIcons.computeIfAbsent(transportType, t -> loadIcon(TRASPORT_TYPE_PATHS.get(t)));
    }

    private static ImageIcon loadIcon(final String path) {
        final URL resourceUrl =
                IconRegistryImpl.class.getResource(Objects.requireNonNull(path, "icon path cannot be null"));
        return new ImageIcon(Objects.requireNonNull(resourceUrl, "resource not found"));
    }
}
