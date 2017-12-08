package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author sahil
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        //switch for commands and instantiating gitlet?
        //or just call Command.method and do it on the node class created?
        //create new gitlit- if there is old one copy it over?

        // FILL THIS IN
//        try {
            int len = args.length;
            if (len == 0) {
                System.out.println("Please enter a command.");
                return;
            }
            String command = args[0];
            Git gitlit = null;
            try {

                if (Git.prevGit() != null) {
                    gitlit = Git.prevGit();

                } else {
                    gitlit = new Git();
                }
            } catch (Exception e) {
                System.out.println("fml");
                System.exit(0);
            }
//        Git gitlit = new Git();   //could be onto something
            //LOAD previously saved by transferring info??
            //find out where i am
            //if not in working directory and not init command give error??????


//        if (!command.equals("init")) {
//            File a = new File(".gitlet"); //does this check direcoties too?
//            if (!a.exists()) {
//                System.out.println("gitlet directory does not exists.");   //can you call this from the main?
//                return;
//            }
//        }
            if (gitlit == null) {
                System.out.println("issue here");
                System.exit(0);
            }
            switch (command) {
                case "init":
                    if (len != 1) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    gitlit.init();
                    break;


                case "add":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.add(args[1]);
                    break;


                case "commit":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    //if no messages error case
                    gitlit.commit(args[1]);
                    break;


                case "rm":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.remove(args[1]);
                    break;


                case "log":
                    if (len != 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.log();
                    break;


                case "global-log":
                    if (len != 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.globalLog();
                    break;


                case "find":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.find(args[1]);
                    break;


                case "status":
                    if (len != 1) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.status();
                    break;


                case "checkout":           //check the indicies and shit
                    if (len == 2) {
                        gitlit.checkout2(args[1]);
                        break;
                    }
                    if (len == 3) {
                        gitlit.checkout1(args[2]);
                        break;
                    }
                    if (len == 4) {
                        gitlit.checkout3(args[1], args[3]);
                        break;
                    } else {
                        System.out.println("Incorrect operands.");
                        return;
                    }

                case "branch":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.branch(args[1]);
                    break;

                case "rm-branch":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.rmBranch(args[1]);
                    break;


                case "reset":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.reset(args[1]);
                    break;


                case "merge":
                    if (len != 2) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    gitlit.merge(args[1]);
                    break;

                default:
                    System.out.println("No command with that name exists.");
                    break;
            }

            Git.saveGit(gitlit);

            //save object?


            //gameplan: 1) create gitlet class-- copy old one if it exists. (should be saved in a file within .gitlet subdir)?
            // if not create new one. Need to find out what vars/maps/arrays are needed for gitlet object
            //create class for node- also find out what is needed to represent node
            //create class for tree- represent nodes and keep apropriate pointers?


//        } catch (Exception e) {
//            System.out.println("error heere.");
//            System.exit(0);
//        }
    }

}
