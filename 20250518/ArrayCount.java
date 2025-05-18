import java.util.ArrayList;
import java.util.Scanner;
public class ArrayCount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = 0,last = Integer.MAX_VALUE;
        System.out.println("Enter the elements of the array, end with '+' :");
        while(sc.hasNextInt()) {
            int n = sc.nextInt();
            if(n!=last){
                count++;
                last = n;
            }
        }
        System.out.println("The number of distinct elements in the array is: " + count);
    }
}
