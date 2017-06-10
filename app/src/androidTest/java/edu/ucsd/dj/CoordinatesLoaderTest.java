package edu.ucsd.dj;

import org.junit.Test;

import edu.ucsd.dj.others.CoordinatesLoader;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jonathanjimenez on 5/14/17.
 */

public class CoordinatesLoaderTest {

    @Test
    public void testFormat() throws Exception {
        assertEquals(23.41611111111111, CoordinatesLoader.format("23/1,24/1,1458/25", "N"));
        assertEquals(-23.41611111111111, CoordinatesLoader.format("23/1,24/1,1458/25", "S"));
        assertEquals(16.697777777777777, CoordinatesLoader.format("16/1,41/1,164451/3125", "E"));
        assertEquals(-16.697777777777777, CoordinatesLoader.format("16/1,41/1,164451/3125", "W"));
        assertEquals(16.697777777777777, CoordinatesLoader.format("16.697777777777777", "E"));
        assertEquals(-16.697777777777777, CoordinatesLoader.format("16.697777777777777", "W"));
    }

}
