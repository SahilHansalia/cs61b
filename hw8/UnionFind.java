import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @author sahil h
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        list = new int[N];
        for (int i = 0; i < N; i++) {
            list[i] = i + 1;
        }
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        int part = v;
        while (part != list[part - 1]) {
            part = list[part - 1];
        }
        list[v - 1] = part;
        return part;  // FIXME
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        int uLoc = find(u);
        int vLoc = find(v);
       if (samePartition(u,v)) {
           return uLoc;
       }
       else {
           list[uLoc - 1] = vLoc;
           return uLoc;
       }
//       else if (uLoc > vLoc) {
//           list[uLoc] = vLoc;
//           return uLoc;
//       }
//       else {
//           list[vLoc] = uLoc;
//           return vLoc;
//       }
    }

    int[] list;
    int sets;
}
