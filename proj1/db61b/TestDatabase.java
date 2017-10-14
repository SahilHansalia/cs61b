package db61b;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDatabase {

    @Test
    public void testDatabase() {
        Database a = new Database();
        Table table = new Table(new String[] {"a", "b", "c"});
        a.put("d", table);
        assertEquals(table, a.get("d"));
    }
}

