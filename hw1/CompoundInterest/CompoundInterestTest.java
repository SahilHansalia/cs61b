import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {

        assertEquals(5, CompoundInterest.numYears(2020));
        assertEquals(1, CompoundInterest.numYears(2016));
        assertEquals(0, CompoundInterest.numYears(2015));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(10, 12, 2017), 12.544, tolerance);
    }



    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2017,3), 11.80, tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavings(5000, 2017, 10), 16550, tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 10;
        assertEquals(CompoundInterest.totalSavingsReal(5000, 2017, 10, 3), 15600, tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
