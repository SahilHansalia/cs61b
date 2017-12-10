package gitlet;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/** class representing commits which can be thought of as
 * nodes in the overall commit tree.
 *  @author sahil
 */
public class Commit implements Serializable {


    /** Commit String. */
    private String name;
    /** Commit Date. */
    private Date date;
    /** Commit datestring. */
    private String dateStr;
    /** Commit parent1. */
    private Commit parent;
    /** COmmit partne2. */
    private Commit parent2;
    /** files. */
    private HashSet<String> files = new HashSet<>();
    /** files from stage. */
    private HashSet<String> filesFromStage = new HashSet<>();
    /** merged bool. */
    private boolean merged;

    /** method to convert Commit object to sha hash.
     * @param message is commit
     * @param first is commit
     * @param parentOne is commit
     * @param parentTwo is commit
     * @param stage is commit
     * @param deleteMarks is commit
     * @param merged2 is merged
     * */
    Commit(String message, boolean first, Commit parentOne, Commit parentTwo,
           HashSet<String> stage, HashSet<String> deleteMarks, boolean merged2) {
        merged = merged2;
        parent = parentOne;
        parent2 = parentTwo;
        this.name = message;
        filesFromStage.addAll(stage);
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
        if (!stage.isEmpty()) {
            files.addAll(stage);
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


    /** @return commit */
    public Commit getParent1() {
        return parent;
    }


    /** @return commit */
    public Commit getParent2() {
        return parent2;
    }

    /** @return String */
    public String getName() {
        return name;
    }

    /**  @return date */
    public Date getDate() {
        return date;
    }

    /** @return String */
    public String getDateStr() {
        return dateStr;
    }

    /** @return hashset */
    public HashSet<String> getFiles() {
        return files;
    }

    /** @return hashset */
    public HashSet<String> getFilesFromStage() {
        return filesFromStage;
    }

    /** @return merged bool */
    public boolean getMerged() {
        return merged;
    }







}
