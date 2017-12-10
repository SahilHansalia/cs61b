package gitlet;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/** class responsible for processing commands given to gitlet.
 *  @author sahil
 */
public class Git implements Serializable {


    /** Git Hashmap. */
    private HashMap<String, HashSet<String>>
            branchTocommitSHA = new HashMap<>();
    /** Commit String. */
    private String headBranch;
    /** Git Hashmap. */
    private HashMap<String, HashSet<String>> messagetoID = new HashMap<>();
    /** Git Hashmap. */
    private HashMap<String, String> idtoMessage = new HashMap<>();
    /** Git Hashmap. */
    private HashMap<String, String> branchTocommitHeadSHA = new HashMap<>();
    /** Git Hashset. */
    private HashSet<String> stage = new HashSet<>();
    /** Git Hashset. */
    private HashSet<String> branches = new HashSet<>();
    /** Git Hashmap. */
    private HashMap<String, Commit> shatoCommit = new HashMap<>();
    /** Git string. */
    private String shaHead;
    /** Git directory. */
    private File currDir = new File("").getAbsoluteFile();
    /** Git Hashset. */
    private HashSet<String> deleteMarks = new HashSet<>();
    /** Git Hashset. */
    private HashSet<String> modsNotStaged = new HashSet<>();
    /** Git bool. */
    private boolean merged = false;
    /** Git string. */
    private String parent2 = "";

    /** Git constructor. */
    public Git() {
    }
    /** Git saver.
     * @param toSave Git object */
    public static void saveGit(Git toSave) {
        try {
            File saveGitlet = new File(".gitlet/savedgit.ser");
            FileOutputStream s = new FileOutputStream(saveGitlet);
            ObjectOutputStream obj = new ObjectOutputStream(s);
            obj.writeObject(toSave);
            obj.close();
            s.close();
        } catch (IOException e) {
            System.out.println("IOException when trying to save Gitlet.");
        }
    }
    /** Git saver.
     * @return Git object */
    public static Git prevGit() {
        Git prev = null;
        File dir = new File(".gitlet/savedgit.ser");
        if (dir.exists()) {
            try {
                FileInputStream filein = new
                        FileInputStream(".gitlet/savedgit.ser");
                ObjectInputStream objin = new ObjectInputStream(filein);
                prev = (Git) objin.readObject();
                filein.close();
                objin.close();
            } catch (IOException e) {
                System.out.println("IOException "
                        + "when trying to read saved git.");
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException"
                        + " when trying to read saved git.");
                System.exit(0);
            }
        }
        return prev;

    }
    /** Git initializer. */
    public void init() {
        File git = new File(".gitlet/");
        if (!git.exists()) {
            git.mkdir();
            Commit first = new Commit("initial commit", true,
                    null, null, stage, deleteMarks, false);
            String sha = SHAconverter.converter(first);
            HashSet<String> toAdd = new HashSet<>();
            toAdd.add(sha);
            branches.add("master");
            branchTocommitSHA.put("master", toAdd);
            branchTocommitHeadSHA.put("master", sha);
            headBranch = "master";
            HashSet<String> lst = new HashSet<>();
            lst.add(sha);
            messagetoID.put("initial commit", lst);
            idtoMessage.put(sha, "initial commit");
            shatoCommit.put(sha, first);
            shaHead = sha;
            saveCommit(first, sha);

        } else {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
        }
    }

    /** Adds file to be staged.
     * @param fileName Git object */
    public void add(String fileName) {
        File check = new File(fileName);
        if (!check.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (deleteMarks.contains(fileName)) {
            deleteMarks.remove(fileName);
            return;
        }
        if ((shatoCommit.get(shaHead).getFiles().contains(fileName))) {
            File old = new File(".gitlet/" + shaHead + "/" + fileName);
            File curr = new File(fileName);
            if (Utils.readContentsAsString(old)
                    .equals(Utils.readContentsAsString(curr))) {
                if (stage.contains(fileName)) {
                    stage.remove(fileName);
                }
                System.exit(0);
            }
        }
        stage.add(fileName);
    }

    /** Git saver.
     * @param c Git object
     * @param sha string*/
    private void saveCommit(Commit c, String sha) {

        File toAdd = new File(".gitlet/" + sha + "/");
        toAdd.mkdirs();
        String fromDir = "";
        for (String fileName : c.getFiles()) {
            if (c.getFilesFromStage().contains(fileName)) {
                fromDir = fileName;
            } else {
                String parentSHA = SHAconverter.converter(c.getParent1());
                fromDir = ".gitlet/" + parentSHA + "/" + fileName;
            }
            File from = new File(fromDir);
            File to = new File(".gitlet/" + sha + "/" + fileName);
//            to.mkdir();
            try {
                Files.copy(from.toPath(), to.toPath(), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("oops");
//                System.out.println(from.toPath());
//                System.out.println(to.toPath());
//                System.out.println(c.getFiles().toString());
//                System.out.println(Utils.plainFilenamesIn(currDir).toString());
//                System.out.println("error here");
                continue;
            }
        }
    }

    /** Git commiter.
     * @param message string */
    public void commit(String message) {

        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        if (stage.isEmpty() && deleteMarks.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit toAdd;
        if (merged) {
            toAdd = new Commit(message,
                    false, shatoCommit.get(shaHead),
                    shatoCommit.get(branchTocommitHeadSHA.get(parent2)),
                    stage, deleteMarks, true);
        } else {
            toAdd = new Commit(message,
                    false, shatoCommit.get(shaHead),
                    null, stage, deleteMarks, false);
        }
        parent2 = "";
        merged = false;
        String sha = SHAconverter.converter(toAdd);
        branchTocommitHeadSHA.put(headBranch, sha);
        if (branchTocommitSHA.containsKey(headBranch)) {
            branchTocommitSHA.get(headBranch).add(sha);
        } else {
            HashSet<String> lst2 = new HashSet<>();
            lst2.add(sha);
            branchTocommitSHA.put(headBranch, lst2);
        }
        idtoMessage.put(sha, message);
        if (messagetoID.containsKey(message)) {
            messagetoID.get(message).add(sha);
        } else {
            HashSet<String> lst = new HashSet<>();
            lst.add(sha);
            messagetoID.put(message, lst);
        }
        shatoCommit.put(sha, toAdd);
        idtoMessage.put(sha, message);

        stage.clear();
        deleteMarks.clear();
        shaHead = sha;
        saveCommit(toAdd, sha);
    }


    /** Git remover.
     * @param fileName string */
    public void remove(String fileName) {
        if (stage.contains(fileName)) {
            stage.remove(fileName);
            return;
        }
        if (shatoCommit.get(shaHead).getFiles().contains(fileName)) {
            deleteMarks.add(fileName);
            Utils.restrictedDelete(fileName);
            return;

        } else if (!stage.contains(fileName)
                && !shatoCommit.get(shaHead).getFiles().contains(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }
    /** Git commitPrinter.
     * @param c Commit */
    private void commitPrinter(Commit c) {
        String b1 = "";
        String b2 = "";
        System.out.println("===");
        System.out.println("commit " + SHAconverter.converter(c));
        if (c.getParent2() != null) {
            String p1SHA = SHAconverter.converter(c.getParent1());
            String p2SHA = SHAconverter.converter(c.getParent2());


            if (branchTocommitSHA.get(headBranch).contains(p1SHA)) {
                b1 = headBranch;
                for (String S : branches) {
                    if (branchTocommitSHA.get(S).contains(p2SHA)) {
                        b2 = S;
                        break;
                    }
                }
                System.out.println("Merge: " + p1SHA.substring(0, 7)
                        + " " + p2SHA.substring(0, 7));


            } else if (branchTocommitSHA.get(headBranch).contains(p2SHA)) {
                b2 = headBranch;
                for (String S: branches) {
                    if (branchTocommitSHA.get(S).contains(p2SHA)) {
                        b1 = S;
                        break;
                    }
                }
                System.out.println("Merge:" + p2SHA.substring(0, 7)
                        + " " + p1SHA.substring(0, 7));
            }
        }
        SimpleDateFormat formatter = new
                SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        System.out.println("Date: " + formatter.format(c.getDate()));
        if (c.getParent2() != null) {
//            System.out.println("Merged " + b1 + " into " + b2 + ".");
        }
        System.out.println(idtoMessage.get(SHAconverter.converter(c)));
    }


    /** Git logger. */
    public void log() {
        Commit curr = shatoCommit.get(shaHead);
        while (curr != null) {
            commitPrinter(curr);
            System.out.println();
            curr = curr.getParent1();
        }
    }


    /** Git global logger. */
    public void globalLog() {
        for (String sha : shatoCommit.keySet()) {
            commitPrinter(shatoCommit.get(sha));
            System.out.println();
        }
    }

    /** Git searcher.
     * @param message string */
    public void find(String message) {
        if (!messagetoID.containsKey(message)) {
            System.out.println("Found no commit with that message.");
            return;
        }
        for (String S: messagetoID.get(message)) {
            System.out.println(S);
        }
    }
    /** Git status updater. */
    public void status() {
        ArrayList<String> sorter = new ArrayList<>();
        sorter.addAll(branches);
        Collections.sort(sorter);
        sorter.set(sorter.indexOf(headBranch), "*" + headBranch);
        System.out.println("=== Branches ===");
        for (String S : sorter) {
            System.out.println(S);
        }
        System.out.println();
        ArrayList<String> sorter2 = new ArrayList<>();
        sorter2.addAll(stage); System.out.println("=== Staged Files ===");
        for (String S : sorter2) {
            System.out.println(S);
        } System.out.println();
        ArrayList<String> sorter3 = new ArrayList<>();
        sorter3.addAll(deleteMarks);
        System.out.println("=== Removed Files ===");
        for (String S : sorter3) {
            System.out.println(S);
        } System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (shatoCommit.get(shaHead).getFiles().contains(fileName)
                        && shatoCommit.get(shaHead).getFiles().size() == 1
                        && shatoCommit.get(shaHead).getParent1().
                        getName().equals("initial commit")) {
                    System.out.println(fileName + " (modified");
                    modsNotStaged.add(fileName);
                }
            }
        }
        if (shatoCommit.get(shaHead).getFiles() != null) {
            for (String fileName : shatoCommit.get(shaHead).getFiles()) {
                if (Utils.plainFilenamesIn(currDir) == null
                        && shatoCommit.get(shaHead).getFiles().size() == 1
                        && shatoCommit.get(shaHead).getParent1().
                        getName().equals("initial commit")) {
                    System.out.println(fileName + " (deleted)");
                } else {
                    if (!Utils.plainFilenamesIn(currDir).contains(fileName)
                            && shatoCommit.get(shaHead).
                            getFiles().size() == 1) {
                        System.out.println(fileName + " (deleted)");
                    }
                }
            }
        }
        System.out.println(); System.out.println("=== Untracked Files ===");
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (!stage.contains(fileName)
                        && !shatoCommit.get(shaHead).
                        getFiles().contains(fileName)) {
                    System.out.println(fileName);
                }
            }

        }
    }



    /** Git checkout for filenames.
     * @param fileName string */
    public void checkout1(String fileName) {
        if (!shatoCommit.get(shaHead).getFiles().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            try {
                Files.copy(Paths.get(".gitlet/" + shaHead + "/" + fileName),
                        Paths.get(fileName), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("IOException when trying to checkout "
                        + fileName + " from commit");
                System.exit(0);
            }
        }
    }
    /** Git checkout for branch names.
     * @param branchName string */
    public void checkout2(String branchName) {
        if (!branches.contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (headBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch. ");
            System.exit(0);
        }
        if (Utils.plainFilenamesIn(currDir) != null) {

            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (fileName.equals("savedgit.ser")) {
                    continue;
                }
                if (!shatoCommit.get(shaHead).getFiles().contains(fileName)
                        && !stage.contains(fileName)) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it or add it first.");
                    System.exit(0);
                }
            }
        }

        String sha = branchTocommitHeadSHA.get(branchName);
        Commit c = shatoCommit.get(sha);

        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (fileName.equals("savedgit.ser")) {
                    continue;
                }
                if (!c.getFiles().contains(fileName)) {
                    Utils.restrictedDelete(fileName);
                }
            }
        }
        for (String fileName : c.getFiles()) {
//            File f = new File(fileName);
//            if (f.exists()) {
////                System.out.println("exists");
//            }
            File to = new File(fileName);
            if (!to.exists()) {
                try {to.createNewFile(); }
                catch (IOException e) {
                    System.out.println("fml");
                }
            }
            File from = new File(".gitlet/" + sha + "/" + fileName);
            try {
                Files.copy(from.toPath(), to.toPath(), REPLACE_EXISTING);
//                System.out.println("completes once");
            } catch (IOException e) {
//                System.out.println(c.getFiles().toString());
//                System.out.println(Utils.plainFilenamesIn(currDir).toString());
//                System.out.println("IOException when trying to checkout "
//                        + fileName + " from commit");
                continue;
            }
//            try {
//                Files.copy(Paths.get(".gitlet/" + sha + "/" + fileName),
//                        Paths.get(fileName), REPLACE_EXISTING);
//            } catch (IOException e) {
//                System.out.println(c.getFiles().toString());
//                System.out.println(Utils.plainFilenamesIn(currDir).toString());
//                System.out.println("IOException when trying to checkout "
//                        + fileName + " from commit");
//                System.exit(0);
//            }
        }
        headBranch = branchName;
        shaHead = branchTocommitHeadSHA.get(branchName);
        stage.clear();
    }

    /** Git checkout 3.
     * @param fileName string
     * @param commitID string*/
    public void checkout3(String commitID, String fileName) {
        int len = commitID.length();
        String shaKey = "";
        for (String S : shatoCommit.keySet()) {
            if (S.substring(0, len).equals(commitID)) {
                shaKey = S;
                break;
            }
        }
        if (shaKey.equals("")) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit c = shatoCommit.get(shaKey);
        if (!c.getFiles().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        try {
            Files.copy(Paths.get(".gitlet/" + shaKey + "/" + fileName), //what if file was removed?
                    Paths.get(fileName), REPLACE_EXISTING);
        } catch (IOException e) {
            return;
        }
    }

    /** Git brancher.
     * @param branchName string */
    public void branch(String branchName) {
        if (branches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branches.add(branchName);
        branchTocommitHeadSHA.put(branchName, shaHead);
    }

    /** Git branch remover.
     * @param branchName string */
    public void rmBranch(String branchName) {
        if (!branches.contains(branchName)) {
            System.out.println("A branch with that name does not exists.");
            System.exit(0);
        }
        if (branchName.equals(headBranch)) {
            System.out.println("Cannot remove the current branch");
            System.exit(0);
        }
        branches.remove(branchName);
        branchTocommitHeadSHA.remove(branchName);
    }

    /** Git reseter.
     * @param commitID string */
    public void reset(String commitID) {
        int len = commitID.length();
        String shaKey = "";
        for (String S : shatoCommit.keySet()) {
            if (S.substring(0, len).equals(commitID)) {
                shaKey = S;
                break;
            }
        }
        if (shaKey.equals("")) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (fileName.equals("savedgit.ser")) {
                    continue;
                }
                if (!shatoCommit.get(shaHead).getFiles().contains(fileName)
                        && !stage.contains(fileName)) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it or add it first.");
                    System.exit(0);
                }
            }
        }

        branchTocommitHeadSHA.put(headBranch, shaKey);
        shaHead = shaKey;
    }

    /** Git merger.
     * @param branchName string */
    public void merge(String branchName) {
        if (!stage.isEmpty() | !deleteMarks.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!branches.contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(headBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        for (String fileName : Utils.plainFilenamesIn(currDir)) {
            if (!shatoCommit.get(shaHead).getFiles().contains(fileName)) {
                System.out.println("There is an untracked file in the way; delete it or add it first.");   //not sure if this works- consider stage too?
                return;
            }
        }
        String splitSHA = splitpointSHA(branchName);

        if (splitSHA.equals(branchTocommitHeadSHA.get(branchName))) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitSHA.equals(shaHead)) {
            branchTocommitHeadSHA.put(headBranch, branchTocommitHeadSHA.get(branchName));
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        String mergeBranchSHA = branchTocommitHeadSHA.get(branchName);
        HashSet<String> filesInSplit = shatoCommit.get(splitSHA).getFiles();
        HashSet<String> filesInCurr = shatoCommit.get(shaHead).getFiles();
        HashSet<String> filesInGiven = shatoCommit.get(mergeBranchSHA).getFiles();
        HashSet<String> inEither = new HashSet<>();
        inEither.addAll(filesInCurr);
        inEither.addAll(filesInGiven);
        inEither.addAll(filesInSplit);
        List<String> filesInDir = Utils.plainFilenamesIn(currDir);
        String mergedmessage = ("Merged " + branchName + " into " + headBranch + ".");
        merged = true;
        parent2 = branchName;
        boolean conflict = false;
//        System.out.println(inEither.toString());
//        System.out.println(filesInCurr.toString());
//        System.out.println(filesInSplit.toString());

        for (String fileName : inEither) {

            if (!filesInSplit.contains(fileName)) {
                if (filesInCurr.contains(fileName) && !filesInGiven.contains(fileName)) {
//                    System.out.println(fileName);
                    continue;
                }
                if (filesInGiven.contains(fileName) && !filesInCurr.contains(fileName)) {
//                    System.out.println(fileName);
                    checkout3(mergeBranchSHA, fileName);
                    stage.add(fileName); //use ADD if deletemarks issues
                    continue;
                }
                if (!fileComparer(shaHead, mergeBranchSHA, fileName)) {
                    conflict = true;
                    conflictMerger(shaHead, mergeBranchSHA, fileName);
                    continue;
                    //merge conflict
                }
            }
            else {
                if (fileComparer(mergeBranchSHA, shaHead, fileName) | (!filesInCurr.contains(fileName) && !filesInGiven.contains(fileName))) {
//                    System.out.println(fileName);
                    continue;
                }
//                System.out.println(fileComparer(splitSHA, shaHead, fileName) + fileName);
//                System.out.println(!filesInGiven.contains(fileName) + fileName);

                if (fileComparer(splitSHA, shaHead, fileName) && !filesInGiven.contains(fileName)) {
//                    System.out.println(fileName);
                    remove(fileName);
                    continue;
                }
                if (fileComparer(splitSHA, mergeBranchSHA, fileName) && !filesInCurr.contains(fileName)) {
                    continue;
                }
                if (!fileComparer(mergeBranchSHA, splitSHA, fileName) && fileComparer(shaHead, splitSHA, fileName)) {
                    checkout3(mergeBranchSHA, fileName);
                    stage.add(fileName);  //manual stage ok?
                    continue;
                }
                if ((filesInCurr.contains(fileName) && filesInGiven.contains(fileName))
                        && (!fileComparer(mergeBranchSHA, shaHead, fileName))
                        && (!fileComparer(splitSHA, shaHead, fileName))
                        && (!fileComparer(mergeBranchSHA, splitSHA, fileName))) {
                    conflict = true;
                    conflictMerger(shaHead, mergeBranchSHA, fileName);
                    continue;
                }
                if ((!fileComparer(splitSHA, mergeBranchSHA, fileName) && !filesInCurr.contains(fileName))
                        | (!fileComparer(splitSHA, shaHead, fileName) && !filesInGiven.contains(fileName))) {
                    conflict = true;
                    conflictMerger(shaHead, mergeBranchSHA, fileName);
                    continue;
                }

            }
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }

        commit(mergedmessage);




    }

    /** Splitpoint helper.
     * @param currHead string
     * @param mergeHead string
     * @param fileName string*/
    private void conflictMerger(String currHead, String mergeHead, String fileName) {
        String currConents = "";
        String givenContents = "";
        if (shatoCommit.get(currHead).getFiles().contains(fileName)) {
            File curr = new File(".gitlet/" + currHead + "/" + fileName);
            currConents = Utils.readContentsAsString(curr);
        }
        if (shatoCommit.get(mergeHead).getFiles().contains(fileName)) {
            File given = new File(".gitlet/" + mergeHead + "/" + fileName);
            givenContents = Utils.readContentsAsString(given);
        }
//        System.out.println(currConents);
//        System.out.println(givenContents);
//        File curr = new File(".gitlet/" + currHead + "/" + fileName);
//        File given = new File(".gitlet/" + mergeHead + "/" + fileName);
//        String currConents = Utils.readContentsAsString(curr);
//        String givenContents = Utils.readContentsAsString(given);
        String contents = ("<<<<<<< HEAD\n" + currConents + "=======\n" + givenContents + ">>>>>>>\n");
        File f = new File(fileName);
        Utils.writeContents(f, contents);
    }





    /** Splitpoint helper.
     * @param sha1 string
     * @param sha2 string
     * @param fileName string
     * @return whether files are same*/
    boolean fileComparer(String sha1, String sha2, String fileName) {
        if(!shatoCommit.get(sha1).getFiles().contains(fileName) | !shatoCommit.get(sha2).getFiles().contains(fileName)) {
            return false;
        }
        try {
        File one = new File(".gitlet/" + sha1 + "/" + fileName);
        File two = new File(".gitlet/" + sha2 + "/" + fileName);
//            System.out.println(Utils.plainFilenamesIn(".gitlet/" + sha1 + "/"));
//            System.out.println(Utils.plainFilenamesIn(".gitlet/" + sha2 + "/"));
        return Utils.readContentsAsString(one).equals(Utils.readContentsAsString(two)); }
        catch (IllegalArgumentException e) {
            System.out.println("errors oops");
            return false;
        }
    }


    /** Splitpoint helper.
     * @param branchName string
     * @return len of commit chain*/
    public int branchLen(String branchName) {
        Commit c = shatoCommit.get(branchTocommitHeadSHA.get(branchName));
        int len = 0;
        while (c != null) {
            len = len + 1;
            c = c.getParent1();
        }
        return len;
    }

    public String splitpointSHA(String branchName) {
        String longer = "";
        String shorter = "";
        if (branchLen(branchName) > branchLen(headBranch)) {
            longer = branchName;
            shorter = headBranch;

        }
        else {
            longer = headBranch;
            shorter = branchName;
        }
//        System.out.println(branchLen(longer));
//        System.out.println(branchLen(shorter));
        Commit cLong = shatoCommit.get(branchTocommitHeadSHA.get(longer));
        for (int i = 0; i < (branchLen(longer) - branchLen(shorter)); i++) {
            cLong = cLong.getParent1();
        }
        Commit cShort = shatoCommit.get(branchTocommitHeadSHA.get(shorter));

        while (!SHAconverter.converter(cLong).equals(SHAconverter.converter(cShort))) {
//            System.out.println(SHAconverter.converter(cLong));
//            System.out.println(SHAconverter.converter(cShort));
//            System.out.println(cLong.getFiles());
            cLong = cLong.getParent1();
//            System.out.println(cLong.getFiles());
//            System.out.println(cShort.getFiles());
            cShort = cShort.getParent1();

        }
//        System.out.println(cLong.getFiles());
        return SHAconverter.converter(cLong);
    }




}
