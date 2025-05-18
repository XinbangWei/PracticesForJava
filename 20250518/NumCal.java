import java.util.Scanner;
public class NumCal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number, end with '+':");
        while(sc.hasNextInt()) {
            Calculate(sc.nextInt());
            System.out.println();
            System.out.println();
        }
    }
    public static void Calculate(int n)
    {
        if(n%2==0)
        {
            System.out.println("The number "+n+" is even");
            System.out.println("Number of multiples of 2:");
            for(int i=2;i<=n;i+=2){
                System.out.print(i);
                System.out.print(" ");
            }
        }
        else
        {
            System.out.println("The number "+n+" is odd");
            System.out.println("Number of multiples of 3:");
            for(int i=3;i<=n;i+=3){
                System.out.print(i);
                System.out.print(" ");
            }
        }

    }
}
