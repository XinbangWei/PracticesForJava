import java.util.Scanner;

public class RemoveSpace {
    public static void main(String[] args) {
        System.out.print("Enter a string: ");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.print(str.replaceAll(" ",""));
    }
}
