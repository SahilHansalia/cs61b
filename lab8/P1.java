import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class P1 {

    public static void main(String... ignored) {
        Scanner read = new Scanner(System.in);
        //use scanner to find number of rows?
        int rows = 1;
        while (read.nextLine() != null) {
            rows += 1;
        }
        //reset
        read.reset();
        read.nextLine();
        //find cols
        int cols = 0;
        if (read.next().equals("X") || read.next().equals(" ")) {
            cols += 1;
        }
        //reset
        read.reset();
        //find spaces
        int spaces = 0;
        if (read.nextLine().contains(" ")) {
            spaces += 1;

        }




    }


}
