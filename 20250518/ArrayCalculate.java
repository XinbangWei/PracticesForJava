import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;
public class ArrayCalculate {
    public static void main(String[] args){
        ArrayList<Integer> nums = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the elements of the array, end with '+' :");
        while(sc.hasNextInt()) {
            nums.add(sc.nextInt());
        }
        ArrayInfo info = Calculate(nums);
        System.out.println("Average of the array is: " + info.average);
        System.out.println("Max of the array is: " + info.max);
    }
    public static ArrayInfo Calculate(ArrayList<Integer> nums){
        ArrayInfo info = new ArrayInfo();
        info.max = Integer.MIN_VALUE;
        int sum = 0;
        for(int num : nums)
        {
            if(num > info.max){
                info.max = num;
            }
            sum+=num;
        }
        info.average = (double)sum/nums.size();
        return info;
    }
    public static class ArrayInfo{
        double average;
        int max;
    }

}
