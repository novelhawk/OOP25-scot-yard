package it.unibo.scotyard.model.map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class TransportTypeTest {

    @Test
    void allTransportTypesExist() {
        assertNotNull(TransportType.TAXI);
        assertNotNull(TransportType.BUS);
        assertNotNull(TransportType.UNDERGROUND);
        assertNotNull(TransportType.FERRY);
    }

}
