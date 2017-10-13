// This is a SUGGESTED skeleton for a class that contains the Tables your
// program manipulates.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes about 6
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;
import java.util.*;

/** A collection of Tables, indexed by name.
 *  @author Sahil and 61b staff*/
class Database {
    /** An empty database which is represented with the HashMap data structure. */
    public Database() {// written
        _tabledata = new HashMap<>();
    }

    /** Return the Table whose name is NAME stored in this database, or null
     *  if there is no such table. */
    public Table get(String name) {// written
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
        }
        else {
            _tabledata.put(name, table);
            //System.out.print(name);
        }
        System.out.println(_tabledata.keySet());
    }

    private HashMap<String, Table> _tabledata;
}
