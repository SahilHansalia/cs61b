/**
 * Created by test on 9/28/2017.
 */
public class WeirdListHelper extends WeirdList {

    public WeirdListHelper() {
        super(0, null);
    }

    @Override
    public int length() {
        return 0; }

    @Override
        public WeirdList map(IntUnaryFunction func) {
        return new WeirdListHelper();
        }

    @Override
    public String toString() {
        return "";
    }
}
