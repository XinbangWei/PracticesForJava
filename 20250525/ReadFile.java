import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {
	public static void main(String[] args) {
		File inputFile = new File("temp1.txt");
		File outputFile = new File("temp2.txt");

		try (Scanner scanner = new Scanner(inputFile);
		     BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			while (scanner.hasNextLine()) {
				writer.write(scanner.nextLine());
				writer.newLine();
			}
			System.out.println("Finished.");
		} catch (IOException e) {
			System.err.println("Wrong:" + e.getMessage());
		}
	}
}