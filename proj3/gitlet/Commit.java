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
        if (Parent != null) {
            if (!Parent.Files.isEmpty()) {
                Files.addAll(Parent.Files);
            }
        }
        if (!Stage.isEmpty()) {
            Files.addAll(Stage); //properly add files here
        }
        if (Parent != null) {
            for (String fileName : parent.Files) {
                if (!Files.contains(fileName)) {
                    Files.add(fileName);
                }
            }

            //deal with second parent files


        parent = Parent;
        parent2 = Parent2;


        }

    }

    public Commit getParent



}
