package virushade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringManipulatorTest {
    @Test
    public void slashPartitionTest() {
        assertEquals("Before",StringManipulator.slashPartition("Before/After")[0]);
        assertEquals("After", StringManipulator.slashPartition("Before/After")[1]);
    }

    @Test
    public void singleDotTest() {
        assertEquals("Hello", StringManipulator.everythingAfterDot("1.Hello"));
    }

    @Test
    public void doubleDotTest() {
        assertEquals(". Hello", StringManipulator.everythingAfterDot("1.. Hello"));
    }
}
