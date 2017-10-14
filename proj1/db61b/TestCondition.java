package db61b;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestCondition {
    @Test
    public void testCondition() {
        String [] titles1 = new String[] {"a", "b", "c"};
        String [] titles2 = new String[] {"d", "e", "f"};
        Table one = new Table(titles1);
        Table two = new Table(titles2);
        one.add(new String[] {"1", "2", "3"});
        two.add(new String[] {"4", "5", "6"});
        Column col1 = new Column("a", one);
        Column col2 = new Column("d", two);
        Condition a = new Condition(col1, "=", col2);
        Condition c = new Condition(col1, "!=", col2);
        List<Condition> b = new ArrayList<>();
        List<Condition> d = new ArrayList<>();
        b.add(a); d.add(c);
        assertEquals(Condition.test(b, 0, 0), false);
        assertEquals(Condition.test(d, 0, 0), true);
    }

}
