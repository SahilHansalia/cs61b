package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author sahil
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        //switch for commands and instantiating gitlet?
        //or just call Command.method and do it on the node class created.

        // FILL THIS IN
        int len = args.length;
        if (len == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String command = args[0];

        switch(command) {
            case "init":
                if (len != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Git gitlit = new Git();

                //do
            case "add":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "commit":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "rm":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "log":
                if (len != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "global-log":
                if (len != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "find":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "status":
                if (len != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "checkout":
                if (len != 2 && len != 3 && len != 4) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //checkout
            case "branch":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "rm-branch":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "reset":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                //do
            case "merge":
                if (len != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
            default:
                System.out.println("No command with that name exists.");
                return;
                //do
        }







    }

}
