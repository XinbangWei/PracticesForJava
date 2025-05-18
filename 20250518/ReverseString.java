import java.util.Scanner;

public class ReverseString {
    public static void main(String[] args) {
        System.out.println("Enter a string:");
        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        System.out.println(new StringBuilder(str).reverse());
    }
}
