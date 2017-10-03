/**
 * Created by test on 10/2/2017.
 */
public class Add implements IntUnaryFunction {
    private int total = 0;

    public Add(int i) {
        this.total = i;
    }

    public int apply(int j) {
        return total + j;

    }
}
