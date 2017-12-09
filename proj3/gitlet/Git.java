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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/** class responsible for processing commands given to gitlet.
 *  @author sahil
 */
public class Git implements Serializable {


    HashMap<String, HashSet<String>> branchTocommitSHA = new HashMap<>();
    String headBranch;
    HashMap<String, HashSet<String>> messagetoID = new HashMap<>();
    HashMap<String, String> IDtoMessage = new HashMap<>();
    HashMap<String, String> branchTocommitHeadSHA = new HashMap<>();
    HashSet<String> stage = new HashSet<>();
    HashSet<String> branches = new HashSet<>();
    HashMap<String, Commit> SHAtoCommit = new HashMap<>();
    String SHAhead;
    File currDir = new File("").getAbsoluteFile();
    private HashSet<String> deleteMarks = new HashSet<>();
    HashSet<String> modsNotStaged = new HashSet<>();


    public Git() {
    }

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

    public static Git prevGit() {
        Git prev = null;
        File dir = new File(".gitlet/savedgit.ser");
        if (dir.exists()) {
            try {
                FileInputStream filein = new FileInputStream(".gitlet/savedgit.ser");
                ObjectInputStream objin = new ObjectInputStream(filein);
                prev = (Git) objin.readObject();
                filein.close();
                objin.close();
            } catch (IOException e) {
                System.out.println("IOException when trying to read saved git.");
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException when trying to read saved git.");
                System.exit(0);
            }
        }
        return prev;

    }

    public void init() {
        File git = new File(".gitlet/");
        if (!git.exists()) {
            git.mkdir();
            Commit first = new Commit("initial commit", true,
                    null, null, stage, deleteMarks);
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
            IDtoMessage.put(sha, "initial commit");
            SHAtoCommit.put(sha, first);
            SHAhead = sha;
            saveCommit(first, sha);

        } else {
            System.out.println("A Gitlet version-control " +
                    "system already exists in the current directory.");
        }
    }


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
        if ((SHAtoCommit.get(SHAhead).getFiles().contains(fileName))){
            File old = new File(".gitlet/" + SHAhead + "/" + fileName);
            File curr = new File(fileName);
            if (Utils.readContentsAsString(old).equals(Utils.readContentsAsString(curr))) {
                if (stage.contains(fileName)) {
                    stage.remove(fileName);
                }
                System.exit(0);
            }
        }
        stage.add(fileName);
    }


    private void saveCommit(Commit c, String SHA) {

        File toAdd = new File(".gitlet/" + SHA + "/");
        toAdd.mkdir();
        String fromDir = "";
        for (String fileName : c.getFiles()) {
            if (c.getFilesFromStage().contains(fileName)) {
                fromDir = fileName;
            }
            else {
                String parentSHA = SHAconverter.converter(c);
                fromDir = ".gitlet/" + parentSHA + "/" + fileName;
            }
            File from = new File(fromDir);
            File to = new File(".gitlet/" + SHA + "/" + fileName);
            try {
                Files.copy(from.toPath(), to.toPath(), REPLACE_EXISTING);
            }
            catch (IOException e) {
                continue;
            }
        }
    }


    public void commit(String message) {

        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        if (stage.isEmpty() && deleteMarks.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit toAdd = new Commit(message, false, SHAtoCommit.get(SHAhead),
                null, stage, deleteMarks);
        String SHA = SHAconverter.converter(toAdd);
        branchTocommitHeadSHA.put(headBranch, SHA);
        if (branchTocommitSHA.containsKey(headBranch)) {
            branchTocommitSHA.get(headBranch).add(SHA);
        }
        else {
            HashSet<String> lst2 = new HashSet<>();
            lst2.add(SHA);
            branchTocommitSHA.put(headBranch, lst2);
        }
        IDtoMessage.put(SHA, message);
        if (messagetoID.containsKey(message)) {
            messagetoID.get(message).add(SHA);
        }
        else {
            HashSet<String> lst = new HashSet<>();
            lst.add(SHA);
            messagetoID.put(message, lst);
        }
        SHAtoCommit.put(SHA, toAdd);
        IDtoMessage.put(SHA, message);

        stage.clear();
        deleteMarks.clear();
        SHAhead = SHA;
        saveCommit(toAdd, SHA);
    }



    public void remove(String fileName) {
        if (stage.contains(fileName)) {
            stage.remove(fileName);
            return;
        }
        if (SHAtoCommit.get(SHAhead).getFiles().contains(fileName)) {
            deleteMarks.add(fileName);
            Utils.restrictedDelete(fileName);
            return;

        }
        else if (!stage.contains(fileName) && !SHAtoCommit.get(SHAhead).getFiles().contains(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

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
                System.out.println("Merge:" + p1SHA.substring(0, 7) + p2SHA.substring(0, 7));


            } else if (branchTocommitSHA.get(headBranch).contains(p2SHA)) {
                b2 = headBranch;
                for (String S: branches) {
                    if (branchTocommitSHA.get(S).contains(p2SHA)) {
                        b1 = S;
                        break;
                    }
                }
                System.out.println("Merge:" + p2SHA.substring(0, 7) + p1SHA.substring(0, 7));
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        System.out.println("Date: " + formatter.format(c.getDate()));
        if (c.getParent2() != null) {
            System.out.println("Merged " + b1 + " into " + b2 + ".");
        }
        System.out.println(IDtoMessage.get(SHAconverter.converter(c)));
    }



    public void log() {
        Commit curr = SHAtoCommit.get(SHAhead);
        while (curr != null) {
            commitPrinter(curr);
            System.out.println();
            curr = curr.getParent1();
        }
    }



    public void globalLog() {
        for (String SHA : SHAtoCommit.keySet()) {
            commitPrinter(SHAtoCommit.get(SHA));
            System.out.println();
        }
    }


    public void find(String message) {
        if (!messagetoID.containsKey(message)) {
            System.out.println("Found no commit with that message.");
            return;
        }
        for (String S: messagetoID.get(message)) {
            System.out.println(S);
        }
    }

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
        sorter2.addAll(stage);
        System.out.println("=== Staged Files ===");
        for (String S : sorter2) {
            System.out.println(S);
        }
        System.out.println();

        ArrayList<String> sorter3 = new ArrayList<>();
        sorter3.addAll(deleteMarks);
        System.out.println("=== Removed Files ===");
        for (String S : sorter3) {
            System.out.println(S);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (SHAtoCommit.get(SHAhead).getFiles().contains(fileName) &&
                        SHAtoCommit.get(SHAhead).getFiles().size() == 1 && SHAtoCommit.get(SHAhead).getParent1().getName().equals("initial commit")) {
                    System.out.println(fileName + " (modified");
                    modsNotStaged.add(fileName);
                }
            }
        }
        if (SHAtoCommit.get(SHAhead).getFiles() != null) {
            for (String fileName : SHAtoCommit.get(SHAhead).getFiles()) {
                if (Utils.plainFilenamesIn(currDir) == null &&
                        SHAtoCommit.get(SHAhead).getFiles().size() == 1 && SHAtoCommit.get(SHAhead).getParent1().getName().equals("initial commit")) {
                    System.out.println(fileName + " (deleted)");
                }
                else {
                    if (!Utils.plainFilenamesIn(currDir).contains(fileName) &&
                            SHAtoCommit.get(SHAhead).getFiles().size() == 1) {
                        System.out.println(fileName + " (deleted)");
                    }
                }
            }
        }

        System.out.println();
        System.out.println("=== Untracked Files ===");
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (!stage.contains(fileName) &&
                        !SHAtoCommit.get(SHAhead).getFiles().contains(fileName)) {
                    System.out.println(fileName);
                }
            }
        }
    }




    public void checkout1(String fileName) {
        if (!SHAtoCommit.get(SHAhead).getFiles().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            try {
                Files.copy(Paths.get(".gitlet/" + SHAhead + "/" + fileName),
                        Paths.get(fileName), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("IOException when trying to checkout "
                        + fileName + " from commit");
                System.exit(0);
            }
        }
    }

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
                if (!SHAtoCommit.get(SHAhead).getFiles().contains(fileName)
                        && !stage.contains(fileName)) {
                    System.out.println("There is an untracked file in the way; " +
                            "delete it or add it first.");
                    System.exit(0);
                }
            }
        }

        String SHA = branchTocommitHeadSHA.get(branchName);
        Commit c = SHAtoCommit.get(SHA);

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
            try {
                Files.copy(Paths.get(".gitlet/" + SHA + "/" + fileName),
                        Paths.get(fileName), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("IOException when trying to checkout "
                        + fileName + " from commit");
                System.exit(0);
            }
        }
        headBranch = branchName;
        SHAhead = branchTocommitHeadSHA.get(branchName);
        stage.clear();
    }


    public void checkout3(String commitID, String fileName) {
        int len = commitID.length();
        String SHAKey = "";
        for (String S : SHAtoCommit.keySet()) {
            if (S.substring(0, len).equals(commitID)) {
                SHAKey = S;
                break;
            }
        }
        if (SHAKey.equals("")) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit c = SHAtoCommit.get(SHAKey);
        if (!c.getFiles().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        try {
            Files.copy(Paths.get(".gitlet/" + SHAKey + "/" + fileName),
                    Paths.get(fileName), REPLACE_EXISTING);
        } catch (IOException e) {
            return;
        }
    }


    public void branch(String branchName) {
        if (branches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branches.add(branchName);
        branchTocommitHeadSHA.put(branchName, SHAhead);
    }

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


    public void reset(String commitID) {
        int len = commitID.length();
        String SHAKey = "";
        for (String S : SHAtoCommit.keySet()) {
            if (S.substring(0, len).equals(commitID)) {
                SHAKey = S;
                break;
            }
        }
        if (SHAKey.equals("")) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (Utils.plainFilenamesIn(currDir) != null) {
            for (String fileName : Utils.plainFilenamesIn(currDir)) {
                if (fileName.equals("savedgit.ser")) {
                    continue;
                }
                if (!SHAtoCommit.get(SHAhead).getFiles().contains(fileName) &&
                        !stage.contains(fileName)) {
                    System.out.println("There is an untracked file in the way; " +
                            "delete it or add it first.");
                    System.exit(0);
                }
            }
        }

        branchTocommitHeadSHA.put(headBranch, SHAKey);
        SHAhead = SHAKey;
    }


    public void merge(String branchName) {

    }




}
