import java.util.Scanner;
public class ConvertToUpper {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a string:");
        String str = sc.nextLine();
        for(char a:str.toCharArray())
        {
            switch(a)
            {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                    System.out.print((char)(a-32));
                    break;
                default:
                    System.out.print(a);
                    break;
            }
        }
    }
}
