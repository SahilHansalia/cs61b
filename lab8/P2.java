import com.sun.tools.javac.code.Attribute;

import java.util.ArrayList;
import java.util.Scanner;

public class P2 {

    public static void main(String... ignored) {
        Scanner read = new Scanner(System.in);
        ArrayList<String> in_line = new ArrayList<>();
        ArrayList<String> pre_line = new ArrayList<>();
        int pre = 0;
        while (read.hasNext()) {
            pre += 1;
            pre_line.add(read.next());
        }
        read.nextLine();
        int in = 0;
        while (read.hasNext()) {
            in += 1;
            in_line.add(read.next());
        }

        //somehow use distances from end of each letter for
        //either string to weigh???




    }


}
