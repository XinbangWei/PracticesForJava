import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
public class FileAdd
{
	public static void main(String[] args) throws Exception
	{
		Scanner scannera = new Scanner(new File("a.txt"));
		Scanner scannerb = new Scanner(new File("b.txt"));
		FileWriter fileWriter = new FileWriter(new File("c.txt"));

		int a = scannera.nextInt();
		int b = scannerb.nextInt();
		fileWriter.write(String.valueOf(a + b));
		fileWriter.flush();
		fileWriter.close();
		System.out.println("Finished.");
	}
}
