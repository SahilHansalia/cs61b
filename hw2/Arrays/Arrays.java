/* NOTE: The file ArrayUtil.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] a = new int[A.length+ B.length];
        System.arraycopy(A,0,a,0,A.length); System.arraycopy(B,0,a,A.length,B.length);
        return a;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] rem = new int[A.length- len];
        if(len== 0) {
            return A; }
        else {
            System.arraycopy(A,0,rem,0,start);
            System.arraycopy(A,start + len,rem,start,A.length -start -len);

        }

        return rem;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int i = 1;
        int count = 1;
        int k = 0;
        int l = 0;
        while (i < A.length) {
            if (A[i-1] > A[i]) {
                count += 1;
            }
            i += 1;
        }
        int[][] run = new int[count][];

        for (int j = 1; j < A.length; j++) {
            if (A[j-1] > A[j]) {
                run[k+=1]= Utils.subarray(A,l,j-l); l = j;
            }


        }

        return run;
    }
}
