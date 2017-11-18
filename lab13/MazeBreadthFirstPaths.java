
import java.util.*;

/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
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
//
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        queue = new LinkedList<>();
//         Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {

        announce();
        if (s == t) {
            return;
        }
        queue.add(s);
        while (!queue.isEmpty()) {
            Integer pull = queue.remove();
            announce();
            if (pull == t) {
                return;
            }
            marked[pull] = true;
            for (int child : maze.adj(pull)) {
                if (!marked[child]) {
//                    marked[pull] = true;  //here?
                    edgeTo[child] = pull;
                    announce();
                    distTo[child] = distTo[pull] + 1;
                    queue.add(child);
                }
            }
        }
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
    }


    @Override
    public void solve() {
         bfs();
    }
}

