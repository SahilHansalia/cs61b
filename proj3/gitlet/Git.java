package gitlet;


import java.io.Serializable;

/** class responsible for processing commands given to gitlet.
 *  @author sahil
 */
public class Git implements Serializable {




    public Git() {
        //create new Git instance
        //what info do we need?
    }

    public void init() {
        //new tree
        //initial commit (has no files) - single branch master points to initial commimt; master is also current branch
        //initial commit timestamp is given

        //initialize new Git
    }


    public void add(String fileName) {

    }



    public void commit(String message) {

    }



    public void remove(String fileName) {

    }



    public void log() {

    }



    public void globalLog() {

    }


    public void find(String message) {



    }

    public void status() {



    }




    public void checkout1(String fileName) {
    //can be either fileName or branchName
        // how does --- get treated
    }

    public void checkout2(String branchName) {
        //can be either fileName or branchName
        // how does --- get treated
    }


    public void checkout3(String commitID, String fileName) {

    }


    public void branch(String branchName) {

    }

    public void rmBranch(String branchName) {

    }


    public void reset(String commitID) {

    }


    public void merge(String branchName) {

    }




}
