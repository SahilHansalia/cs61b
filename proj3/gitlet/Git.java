package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;

/** class responsible for processing commands given to gitlet.
 *  @author sahil
 */
public class Git implements Serializable {


//    static boolean prevGit = false; //could be an issue?
    HashMap<String, String> branchTocommit = new HashMap<>();
    String headBranch;
    HashMap<String, HashSet<String>> messagetoID = new HashMap<>();
    HashMap<String, String> IDtoMessage = new HashMap<>();
//    HashSet<String> stage = new HashSet<>();
    HashMap<String, String> stage = new HashMap<>();
    HashSet<String> branches = new HashSet<>();
    HashSet<String> removedFiles = new HashSet<>();
    Commit head;
    private HashSet<String> deleteMarks = new HashSet<>();

    //pointer to branch/most recent commit (head)?
    //removed files
    //staged files
    //list of branches'
    //list of marks (to not commit)
    //mapping from ID to messages
    //map from branch name to commit

    public Git() {
        //create new Git instance
        //what info do we need? -- maybe dont need anything here
    }

    public static void save() {
   //serialize and write to file??
        //name the file?


    }

    public static Git prevGit() {
        // get from file that was previously written to
        return new Git();
    }

    public void init() {
        File git = new File(".gitlet");
        if (!git.exists()) {
            git.mkdir();
            Commit first = new Commit("initial commit", true, null, null);
            String sha = new SHAconverter(first).SHA;
            branchTocommit.put("master", sha);
            headBranch = "master";
            HashSet<String> lst = new HashSet<>();
            lst.add(sha);
            messagetoID.put("initial commit", lst);
            IDtoMessage.put(sha, "initial commit");
            head = first;

        } else {
            System.out.println("A gitlet version control system already exists in the current"
                    + " directory.");
            System.exit(0);
        }


        //add serialized commit to hash map with master branch








        //new tree
        //initial commit (has no files) - single branch master points to initial commimt; master is also current branch
        //initial commit timestamp is given

    }


    public void add(String fileName) {
        File check = new File(fileName);
        if (!check.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        if (Utils.sha1(check).equals(head.fileNameToContents.get(fileName))) {
            System.out.println("file has not been modified since last stage");
            if (stage.containsKey(fileName)) {
                stage.remove(fileName);

            }
        }

        if (deleteMarks.contains(fileName)) {
            deleteMarks.remove(fileName);
        }

        else {
            File toAdd = new File(fileName); //is this directory shit correct?
            stage.put(fileName, Utils.readContentsAsString(toAdd));   //does this shit work?


        }

        //check if file exists
        //remove mark if marked
        //if identical to version in current commit- do not stage and remove from stage if there..??

        //stage a blob

    }



    public void commit(String message) { //big issue could be files changed after committing!!

        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        if (stage.isEmpty() && deleteMarks.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit toAdd = new Commit(message, false, head, null);
        String SHA = new SHAconverter(toAdd).SHA;
        branchTocommit.put(headBranch, message);
        IDtoMessage.put(SHA, message);
        if (messagetoID.containsKey(message)) {
            messagetoID.get(message).add(SHA);
        }
        else {
            HashSet<String> lst = new HashSet<>();
            lst.add(SHA);
            messagetoID.put(message, lst);
        }

        stage.clear();
        removedFiles.clear(); //does this go here?


        //deal with new branches??
        head = toAdd;

        //what if commits have the same name?






        //if no changes give error
        //add new commit which is new branch head
        //previous commit is commit's parent commit
        //commit must log date/time and string message AND SHA-1 ID. (abstrat away for now)
        //clear staging area



    }



    public void remove(String fileName) {
        File toRemove = new File(fileName); //right path?
        if (!stage.containsKey(fileName) && !head.Files.contains(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        





        //remove from stage if staged
        //if in prev commit mark to not be commited in next one
        //remove file from working directory (only if it is tracked in current commit)
        //use utils delete
        //if file not staged or not tracked by head commit then error



    }



    public void log() {
        //start with head (most recent commmit) and work backwards to initial parent on single branch
        //most recent on top
        //print out (commit SHA, date, and message)
        //use java.util.Date
        //java.util.Formatter
        //for merge commits (those that have 2 parents)- print first 7 digits of both parents commit IDs





    }



    public void globalLog() {
        //log of all commits but order does not mattere
        //strat: do log on all branches where SHA is different OR have a running list of all unique commits?

    }


    public void find(String message) {
        //prints out ID's of ALL commits with given message



    }

    public void status() {
        //branches (with * by the current one) -- need list of branches
        //staged files
        //removed files







        //modifications not staged for commmit (need to keep track of changed files? (EXTRA CREDIT)
        //untracked = in directory but not staged or tracked (EXTRA CREDIT)
    }




    public void checkout1(String fileName) {
        //~15 lines
        //takes file as it is in head commit and puts it in working directory, overwriting if already there
        //new version not staged (remove if it is staged?)
        //print failure if file doesnt exist


    }

    public void checkout2(String branchName) {
        //failure if: branch is current branch,
        //takes all files in head of branch and puts in working directory
        //given branch now considered head branch
        //Any files that are tracked in the current branch but are not present in the checked-out branch are deleted
        //stage is cleared

    }


    public void checkout3(String commitID, String fileName) {
        //takes file from given commit and places it in working directory.
        //failure if 1) file doesnt exist in commit 2) commit doesnt exist
        //ID is first 6 digits of hash


    }


    public void branch(String branchName) {
        //10 lines?
        //new branch pointing to current head node
        //branchName is name for refrence to SHA code for commit node it points to.
        //if branchNme already exists error

    }

    public void rmBranch(String branchName) {
        //15 lines appx
        //removes pointer to branch with branchName
        //does NOT delete commit nodes
        //fails if branchName DNE or branchName is current branch


    }


    public void reset(String commitID) {
        //~10 lines
        //checks out all files in a given commit
        //removes tracked files not in that commit
        //moves current branch's head to that commit node
        //clears staging

    }


    public void merge(String branchName) {

    }




}
