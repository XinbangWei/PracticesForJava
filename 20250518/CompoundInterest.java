import java.util.Scanner;
import java.lang.Math;
public class CompoundInterest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the principal:");
        double principal = sc.nextDouble();
        System.out.println("Enter the rate(%):");
        double rate = sc.nextDouble();
        System.out.println("Enter the time in years:");
        double years = sc.nextInt();
        System.out.println("Interest:"+(Math.pow(1+rate/100,years)-1)*principal);
        System.out.println("Total:"+Math.pow((1+rate/100),years)*principal);
    }
}
