import java.util.Scanner;

public class PalindromicString
{
    public static void main(String[] args) {
        System.out.print("Enter a string: ");
        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        boolean result = true;
        for (int i = 0; i < str.length() / 2; i++) {
            if (str.charAt(i) != str.charAt(str.length() - i - 1)) result = false;
        }
        System.out.println(result);
    }
}
