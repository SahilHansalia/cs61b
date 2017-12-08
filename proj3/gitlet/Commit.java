package gitlet;


import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    String dateStr;
    Commit parent;
    Commit parent2;
    HashMap<String, String> fileNameToContents = new HashMap<>(); //need to know how to store files in a commit.
    String id;
    HashSet<String> Files = new HashSet<>();
    HashSet<String> FilesfromStage = new HashSet<>();


    Commit(String message, boolean first, Commit Parent, Commit Parent2, HashSet<String> Stage) {  //are the files in a commit = parents files + stage?
        this.name = message;
        FilesfromStage.addAll(Stage);
        SimpleDateFormat formatter;
        if (first) {
//            formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss z", );
            date = new Date(0);
            dateStr = date.toString();
//            .format(new Date(0), "yyyy-MM-dd HH:mm:SS"); //is this all you need?
        } else {
            date = new Date();
            dateStr = date.toString();
//            .format(new Date(), "yyyy-MM-dd HH:mm:SS");
        }
        if (Parent != null) {
            if (!Parent.Files.isEmpty()) {
                Files.addAll(Parent.Files);
            }
        }
        if (!Stage.isEmpty()) {
            Files.addAll(Stage);
        }
        if (Parent != null) {
            if (Parent.Files != null) {
                for (String fileName : Parent.Files) {
                    if (!Files.contains(fileName)) {
                        Files.add(fileName);
                    }
                }
            }

            //deal with second parent files


        parent = Parent;
        parent2 = Parent2;


        }

    }

    public Commit getParent1() {
        return parent;
    }
    public Commit getParent2() {
        return parent2;
    }




}
