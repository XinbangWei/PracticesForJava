import java.util.Arrays;
import java.util.Scanner;
public class SortRactangle
{
	public static void main(String[] args)
	{
		System.out.println("Enter the number of rectangles:");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Rectangle[] rectangles = new Rectangle[n];
		for(int i=0;i<n;i++)
		{
			System.out.println("Enter the length and width of the rectangle:");
			double length = sc.nextDouble();
			double width = sc.nextDouble();
			rectangles[i] = new Rectangle(length, width);
		}
		Arrays.sort(rectangles);
		for(Rectangle r : rectangles)
		{
			System.out.println("Length: " + r.GetLength() + ", Width: " + r.GetWidth() + ", Area: " + r.GetArea());
		}
	}
}

class Rectangle implements Comparable<Rectangle>
{
	private double _length;
	private double _width;
	private double _area;

	public Rectangle(double length, double width)
	{
		this._length = length;
		this._width = width;
		this._area = this._length * this._width;
	}

	public double GetLength() { return _length; }
	public double GetWidth() { return _width; }

	public double GetArea()
	{
		return _area;
	}

	@Override
	public int compareTo(Rectangle other)
	{
		if (this.GetArea() < other.GetArea())
			return 1;
		else if (this.GetArea() > other.GetArea())
			return -1;
		else
			return 0;
	}
}
