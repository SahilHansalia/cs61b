
package db61b;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestTable {
    @Test
    public void testAdd() {
        String[] titles1 = new String[]{"a", "b", "c"};
        String[] titles2 = new String[]{"d", "e", "f"};
        Table one = new Table(titles1);
        Table two = new Table(titles2);
        one.add(new String[]{"1", "2", "3"});
        assertEquals(one.add(new String[]{"7", "8", "9"}), true);
        two.add(new String[]{"4", "5", "6"});
        two.add(new String[]{"10", "11", "12"});
        assertEquals(one.size(), 2);
        assertEquals(two.size(), 2);
    }

    @Test
    public void testGet() {
        String[] titles1 = new String[]{"a", "b", "c"};
        String[] titles2 = new String[]{"d", "e", "f"};
        Table one = new Table(titles1);
        Table two = new Table(titles2);
        one.add(new String[]{"1", "2", "3"});
        one.add(new String[]{"7", "8", "9"});
        two.add(new String[]{"4", "5", "6"});
        two.add(new String[]{"10", "11", "12"});

        assertEquals(one.get(0, 1), "2");
        try {
            two.get(2, 1);
            fail();
        } catch (DBException e) {
            assertTrue(true);
        }


    }

    @Test
    public void testSelect() {
        String [] titles1 = new String[] {"a", "b", "c"};
        String [] titles2 = new String[] {"d", "e", "f"};
        Table one = new Table(titles1);
        Table two = new Table(titles2);
        one.add(new String[] {"1", "2", "3"});
        two.add(new String[] {"4", "5", "6"});
        Column col1 = new Column("a", one);
        Column col2 = new Column("d", two);
        Condition a = new Condition(col1, "!=", "1");
        Condition c = new Condition(col2, "=", "4");
        List<Condition> b = new ArrayList<>();
        List<Condition> d = new ArrayList<>();
        b.add(a); d.add(c);
        List<String> hi = new ArrayList<>();
        for (String letters : titles1) {
            hi.add(letters);
        }
        assertEquals(one.select(hi, d).get(0, 0), "1");
    }
}
