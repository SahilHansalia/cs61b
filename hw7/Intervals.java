import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;
import java.util.*;

/** HW #8, Problem 3.
 *  @author sahil
  */

public class Intervals {


    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals)  {

        intervals.sort(new Comparator<int[]>() {
            public int compare(final int[] a ,int[] b) {
                if (a[0] > b[0]) {
                    return 1;
                }
                else if (a[0] < b[0]) {
                    return -1;
                }
                else {
                    return 0;
                }
            }

        });





        int total = 0;
        int first = 0;
        int second = 0;

        for(int[] i : intervals){
            if(i[0] > second){
                total += (second - first);
                first = i[0];
                second = i[1];
            } else if(i[0] <= second && i[1] > second ){
                second = i[1];
            }
        }
        total += (second - first);
        return total;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
