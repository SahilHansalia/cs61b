package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/** class representing commits which can be thought of as
 * nodes in the overall commit tree.
 *  @author sahil
 */
public class Commit implements Serializable {
    //what files it contains

    String name;
    Date date;
    Commit parent;
    Commit parent2;
    HashMap<String, String> fileNameToContents = new HashMap<>();
    String id;


    Commit(String message, boolean first, Commit Parent) {
        this.name = message;
        if (first) {
            this.date = new Date(0);
        }
        else {
            this.date = new Date();
        }
        parent = Parent;
    }


//node in the commit tree!!!!
    //contains date time it was made
    //

}
