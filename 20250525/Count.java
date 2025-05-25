import java.util.Scanner;
public class Count {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a line of text:");
		String line = sc.nextLine();
		int letters = 0, spaces = 0, digits = 0, others = 0;
		for (char ch : line.toCharArray()) {
			if (Character.isLetter(ch)) {
				letters++;
			} else if (Character.isDigit(ch)) {
				digits++;
			} else if (ch == ' ') {
				spaces++;
			} else {
				others++;
			}
		}
		System.out.println("Letter：" + letters);
		System.out.println("Space：" + spaces);
		System.out.println("Number：" + digits);
		System.out.println("Else：" + others);
	}
}