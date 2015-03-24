package lab3;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Text {

    private String str;

    public Text() {
        str = new String();
    }

    public void Inputtext() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter text: ");
        str = sc.nextLine();
    }

    public String del() {
        StringTokenizer st = new StringTokenizer(str, " ,.");
        String sw = "";
        while (st.hasMoreTokens()) {
            sw = sw + st.nextToken();
        }
        return sw;
    }

    public boolean comp(String s2) {
        String s3 = new StringBuffer(s2).reverse().toString();
        return s2.equalsIgnoreCase(s3);
    }
}
