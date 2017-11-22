import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {    //do
        @Override
        public void sort(int[] array, int k) {
            int ermediate;
            int end = k;
            for (int i = 0; i < k; i++) {
                ermediate = array[i];
                for (int j = i; j > 0; j-- ) {
                    if (array[j] < array[j-1]) {
                        ermediate = array[j];
                        array[j] = array[j-1];
                        array[j-1] = ermediate;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm { //do
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i <k; i++) {
                int smallest = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[smallest] > array[j]) {
                        smallest = j;
                    }
                }
                swap(array, smallest, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm { //do
        @Override
        public void sort(int[] array, int k) {
            sortHelper(array, 0, Math.min(k, array.length));
        }

        public void sortHelper(int[] array, int left, int right) {
            if (left < right) {
                int save = left;
                for (int i = left; i < right; i++) {
                    if (array[i] < array[save]) {
                        left++;
                        swap(array, left, i);
                    }
                }
                swap(array, save, left);
                sortHelper(array, 0, left - 1);
                sortHelper(array, left + 1, right);
            }
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of k-bit numbers.  For
     * example, if you take k to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * k to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of k. */

    /**
     * LSD Sort implementation.
     */
    // worked on this question with people at office hours whose names I forgot to take note of
    public static class LSDSort implements SortingAlgorithm { //do
        @Override
        public void sort(int[] a, int k) {
            /* Each int is 32 bits and are thus 4 byes wide.
               Each byte holds a value between 0 and 255.
               MASK is used to mask off 8 bits at a time */
            int[] copy = new int[k];
            for (int i = 0; i < 4; i++) {

                int[] nums = new int[257];
                for (int j = 0; j < k; j++) {
                    nums[((a[j] >> 8*i) & 255) + 1]++;
                }

                for (int j = 0; j < nums.length-1; j++)
                    nums[j+1] = nums[j+1] + nums[j];

                for (int j = 0; j < k; j++) {
                    copy[nums[(a[j] >> 8*i) & 255]++] = a[j];

                }
                System.arraycopy(copy, 0, a, 0, copy.length);
            }
        }


        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
