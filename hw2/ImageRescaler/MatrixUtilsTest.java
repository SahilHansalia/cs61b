import org.junit.Test;
import static org.junit.Assert.*;

/** tests
 *  @author sahil h
 */

public class MatrixUtilsTest {
    /** FIXME
     */
    @Test
    public void accumulateVerticaltest() {
        double [][]test = new double [2][2];
        test[0][0] = 1;
        test[0][1] = 2;
        test[1][0] = 3;
        test[1][1] = 4;

        double [][] result = new double[2][2];
        result[0][0] = 1;
        result[0][1] = 3;
        result[1][0] = 3;
        result[1][1] = 5;

        assertArrayEquals(MatrixUtils.accumulateVertical(test), result);



    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
