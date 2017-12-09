package gitlet;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/** class representing commits which can be thought of as
 * nodes in the overall commit tree.
 *  @author sahil
 */
public class Commit implements Serializable {

    String name;
    Date date;
    String dateStr;
    Commit parent;
    Commit parent2;
    HashSet<String> Files = new HashSet<>();
    HashSet<String> FilesfromStage = new HashSet<>();


    Commit(String message, boolean first, Commit Parent, Commit Parent2, HashSet<String> Stage, HashSet<String> deleteMarks) {
        this.name = message;
        FilesfromStage.addAll(Stage);
        if (first) {
            date = new Date(0);
            dateStr = date.toString();
        } else {
            date = new Date();
            dateStr = date.toString();
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
        }
        for (String fileName : deleteMarks) {
            Files.remove(fileName);
        }


        parent = Parent;
        parent2 = Parent2;

    }

    public Commit getParent1() {
        return parent;
    }
    public Commit getParent2() {
        return parent2;
    }




}
