import java.util.Scanner;

public class ArraySum {
    public static void main(String[] args) {
        int[] a = new  int[5];
        int sum = 0;
        Scanner input = new Scanner(System.in);
        for(int i = 0; i < a.length; i++){a[i]=input.nextInt();
        sum+=a[i];}
        System.out.println(sum);
    }
}
