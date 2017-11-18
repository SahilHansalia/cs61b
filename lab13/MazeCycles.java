import java.util.Observable;
import java.util.Queue;

/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private Queue<Integer> queue;
    private boolean[] checked;

    public MazeCycles(Maze m) {
        super(m);
        checked = new boolean[m.V()];
    }

    @Override
    public void solve() {
        cycleChecker(0);
        // TODO: Your code here!
    }

    private void cycleChecker(int x) {
        marked[x] = true;
        checked[x] = true;
        try {
            for (int child : maze.adj(x)) {
                if (checked[child]) {
                    if (edgeTo[x] != child) {
                        announce();
                        edgeTo[child] = x;
                        return;
                    }
                }
                if (!marked[child]) {
                    edgeTo[child] = x;
                    cycleChecker(child);
                }
            }
            checked[x] = false;
        }
        catch(NullPointerException E) {
            return;
        }
    }

    // Helper methods go here
}

