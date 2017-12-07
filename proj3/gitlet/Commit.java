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
    HashSet<String> Files = new HashSet<>();


    Commit(String message, boolean first, Commit Parent, Commit Parent2) { //maybe add a stage?  //are the files in a commit = parents files + stage?
        this.name = message;
        if (first) {
            this.date = new Date(0); //is this all you need?
        }
        else {
            this.date = new Date();
        }
        Files.addAll(Parent.Files);
        


        parent = Parent;
        parent2 = Parent2;





    }   //do you need to know what commit a file was added in?
//when do you get a commit ID... ask mihir?

//node in the commit tree!!!!
    //contains date time it was made
    //

}
