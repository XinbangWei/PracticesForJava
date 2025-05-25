import java.util.Arrays;
public class StringSort
{
	public static void main(String[] args)
	{
		String[] cities = {"Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Hangzhou"};
		System.out.println("Before sorting:");
		for (String city : cities)
		{
			System.out.print(city + " ");
		}
		System.out.println();
		Arrays.sort(cities);
		System.out.println("After sorting:");
		for (String city : cities)
		{
			System.out.print(city + " ");
		}
	}
}
