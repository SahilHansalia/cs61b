package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author sahil
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        int len = args.length;
        if (len == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String command = args[0];
        Git gitlit = null;

        if (Git.prevGit() != null) {
            gitlit = Git.prevGit();
        } else {
            gitlit = new Git();
            if (!args[0].equals("init")) {
                System.out.println("Not in an initialized Gitlet directory.");
                System.exit(0);
            }
        }


        switcher(gitlit, args, len);
        Git.saveGit(gitlit);
    }

    /** helper.
     * @param gitlit git object
     * @param args String array
     * @param len number of args*/
    private static void checkoutHandler(Git gitlit, String [] args,
                                        Integer len) {
        if (len == 2) {
            gitlit.checkout2(args[1]);
            return;
        }
        if (len == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            gitlit.checkout1(args[2]);
            return;
        }
        if (len == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            gitlit.checkout3(args[1], args[3]);
            return;
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }

    }
    /** helper.
     * @param len int */
    private static void len2(Integer len) {
        if (len != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
    /** helper.
     * @param len int */
    private static void len1(Integer len) {
        if (len != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }

    }

    /** helper.
     * @param gitlit git object
     * @param args String array
     * @param len number of args*/
    private static void switcher(Git gitlit, String [] args,
                                        Integer len) {
        switch (args[0]) {
        case "init":
            len1(len);
            gitlit.init();
            break;
        case "add":
            len2(len);
            gitlit.add(args[1]);
            break;
        case "commit":
            len2(len);
            gitlit.commit(args[1]);
            break;
        case "rm":
            len2(len);
            gitlit.remove(args[1]);
            break;
        case "log":
            len1(len);
            gitlit.log();
            break;
        case "global-log":
            len1(len);
            gitlit.globalLog();
            break;
        case "find":
            len2(len);
            gitlit.find(args[1]);
            break;
        case "status":
            len1(len);
            gitlit.status();
            break;
        case "checkout":
            checkoutHandler(gitlit, args, len);
            break;
        case "branch":
            len2(len);
            gitlit.branch(args[1]);
            break;
        case "rm-branch":
            len2(len);
            gitlit.rmBranch(args[1]);
            break;
        case "reset":
            len2(len);
            gitlit.reset(args[1]);
            break;
        case "merge":
            len2(len);
            gitlit.merge(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
            break;
        }
    }

}
