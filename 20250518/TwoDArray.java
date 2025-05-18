import java.util.Scanner;
public class TwoDArray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of columns:");
        int cols = sc.nextInt();
        System.out.println("Enter the number of rows:");
        int rows = sc.nextInt();
        int[][] a = new int[cols][rows];
        System.out.println("Enter the elements of the array:");
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for(int i=0;i<cols;i++)
        {
            for(int j=0;j<rows;j++)
            {
                a[i][j] = sc.nextInt();
                if(a[i][j]>max)
                {
                    max = a[i][j];
                }
                if(a[i][j]<min)
                {
                    min = a[i][j];
                }
            }
        }
        System.out.println("The maximum number is " + max);
        System.out.println("The minimum number is " + min);
    }
}
