import org.junit.Test;
import static org.junit.Assert.*;

/** tests array methods
 *  @author sahil h
 */

public class ArraysTest {
    @Test
    public void CatenateTest() {
        int[] one = {1,2,3};
        int[] two = {6,7,8};
        int[] three = {3,2,1};
        int[] four = {};
        int[] ans1 = {1,2,3,6,7,8};
        int[] ans2 = {3,2,1};

        assertArrayEquals(Arrays.catenate(one, two),ans1);
        assertArrayEquals(Arrays.catenate(three, four),ans2);
    }

    @Test
    public void RemoveTest() {
        int[] A = {1, 2, 3, 4, 5, 6};
        int[] B = {1, 2, 5, 6};

        assertArrayEquals(Arrays.remove(A, 2, 2), B);
    }

    @Test
    public void NaturalRunsTest() {
        int[] A = {1, 7,8,9,4,6,8,3};
        int[][] B = new int[3][];
        B[0] = new int[]{1,7,8,9};
        B[1] = new int[]{4,6,8};
        B[2] = new int[]{3};
        assertArrayEquals(Arrays.naturalRuns(A), B);
    }

        public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
