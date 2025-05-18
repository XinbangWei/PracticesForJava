import java.util.Scanner;
public class UpStairs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of stairs:");
        int n = sc.nextInt();
        System.out.println("Number of ways to climb " + n + " stairs:");
        System.out.println(ClimbStairs(n));
    }
    public static int ClimbStairs(int n) {
        if (n <= 1) {
            return 1;
        }
        return ClimbStairs(n - 1) + ClimbStairs(n - 2);
    }
}
