import java.util.ArrayList;
import java.util.Scanner;

public class P3 {

    public static void main(String... ignored) {
        Scanner read = new Scanner(System.in);
        int i = read.nextInt();
        int num = 1;
        int len;
        ArrayList<Integer> output = new ArrayList<Integer>();
        for (int a = i; a > 0; a++) {
            if (output.get(num) != output.get(num-1)) {
                output.add(num);
                num++;
            }
        }



            System.out.print("The smallest good numeral of length " + i + "is" + output);


        }
    }


