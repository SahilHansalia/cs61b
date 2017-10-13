
package db61b;
import java.util.HashMap;


/** A collection of Tables, indexed by name.
 *  @author Sahil and 61b staff*/
class Database {
    /** An empty database which is represented with HashMap data structure. */
    public Database() {
        _tabledata = new HashMap<>();
    }

    /** Return the Table whose name is NAME stored in this database, or null
     *  if there is no such table. */
    public Table get(String name) {
        return _tabledata.get(name);
    }

    /** Set or replace the table named NAME in THIS to TABLE.  TABLE and
     *  NAME must not be null, and NAME must be a valid name for a table. */
    public void put(String name, Table table) {
        if (name == null || table == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (_tabledata.containsKey(name)) {
            _tabledata.replace(name, table);
        } else {
            _tabledata.put(name, table);
        }
    }

    /** New Hash Map. */
    private HashMap<String, Table> _tabledata;
}
