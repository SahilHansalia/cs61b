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
    HashMap<String, String> fileNameToContents = new HashMap<>(); //need to know how to store files in a commit.
    String id;
    HashSet<String> Files = new HashSet<>();


    Commit(String message, boolean first, Commit Parent, Commit Parent2, HashSet<String> Stage) {  //are the files in a commit = parents files + stage?
        this.name = message;
        if (first) {
            this.date = new Date(0); //is this all you need?
        } else {
            this.date = new Date();
        }
        if (!Parent.Files.isEmpty()) {
            Files.addAll(Parent.Files);
        }
        if (!Stage.isEmpty()) {
            Files.addAll(Stage); //properly add files here
            //deal with second parent files and branches here


        parent = Parent;
        parent2 = Parent2;


        }

    }



}
