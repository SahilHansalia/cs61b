/**
 * Created by test on 10/2/2017.
 */
public class Sum implements IntUnaryFunction {
    private int x;

    public Sum() {
    }

    public int apply(int j) {
        x += j;
        return x;
    }

    int getter() {
        return x;
    }

    }



