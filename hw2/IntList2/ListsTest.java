import org.junit.Test;
import static org.junit.Assert.*;

/** Tests Methods written in hw2
 *
 *  @author Sahil H
 */

public class ListsTest {
    @Test
   public void NaturalRunsNormalTest() {
       IntList test = IntList.list(5,8,3,6,7,4,9,6,54);
       assertEquals(IntList2.list(new int[][]{{5, 8}, {3, 6, 7}, {4, 9}, {6, 54}}), Lists.naturalRuns(test));
   }






    // It might initially seem daunting to try to set up
    // Intlist2 expected.
    //
    // There is an easy way to get the IntList2 that you want in just
    // few lines of code! Make note of the IntList2.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
