package it.unibo.scotyard.commons.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

public class SizeImplTest {

    @Test
    void testCreateSize() {
        final Size size = Size.of(800, 600);

        assertEquals(800, size.getWidth());
        assertEquals(600, size.getHeight());
    }

    @Test
    void testCopyCreatesNewInstance() {
        final Size original = Size.of(640, 480);
        final Size copy = original.copy();

        assertEquals(original.getWidth(), copy.getWidth());
        assertEquals(original.getHeight(), copy.getHeight());
        assertNotSame(original, copy);
    }

}
