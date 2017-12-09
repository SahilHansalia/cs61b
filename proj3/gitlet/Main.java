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

                case "checkout":
                    if (len == 2) {
                        gitlit.checkout2(args[1]);
                        break;
                    }
                    if (len == 3) {
                        if (!args[1].equals("--")) {
                            System.out.println("Incorrect operands.");
                            return;
                        }
                        gitlit.checkout1(args[2]);
                        break;
                    }
                    if (len == 4) {
                        if (!args[2].equals("--")) {
                            System.out.println("Incorrect operands.");
                            return;
                        }
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
    }

}
