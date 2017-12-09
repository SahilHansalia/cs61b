package gitlet;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/** class representing commits which can be thought of as
 * nodes in the overall commit tree.
 *  @author sahil
 */
public class Commit implements Serializable {

    private String name;
    private Date date;
    private String dateStr;
    private Commit parent;
    private Commit parent2;
    private HashSet<String> files = new HashSet<>();
    private HashSet<String> filesFromStage = new HashSet<>();


    Commit(String message, boolean first, Commit parentOne, Commit parentTwo, HashSet<String> Stage, HashSet<String> deleteMarks) {
        parent = parentOne;
        parent2 = parentTwo;
        this.name = message;
        filesFromStage.addAll(Stage);
        if (first) {
            date = new Date(0);
            dateStr = date.toString();
        } else {
            date = new Date();
            dateStr = date.toString();
        }
        if (parent != null) {
            if (!parent.files.isEmpty()) {
                files.addAll(parent.files);
            }
        }
        if (!Stage.isEmpty()) {
            files.addAll(Stage);
        }
        if (parent != null) {
            if (parent.files != null) {
                for (String fileName : parent.files) {
                    if (!files.contains(fileName)) {
                        files.add(fileName);
                    }
                }
            }
        }
        for (String fileName : deleteMarks) {
            files.remove(fileName);
        }


    }

    public Commit getParent1() {
        return parent;
    }
    public Commit getParent2() {
        return parent2;
    }

    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public String getDateStr() {
        return dateStr;
    }
    public HashSet<String> getFiles() {
        return files;
    }
    public HashSet<String> getFilesFromStage() {
        return filesFromStage;
    }







}
