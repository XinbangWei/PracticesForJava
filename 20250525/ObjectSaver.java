import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ObjectSaver
{
	public static void main(String[] args)throws Exception
	{
		DeepPerson deepPerson = new DeepPerson("John", 30,
				new Address("New York", "NY", "USA"));
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("temp2.txt")))
		{
			oos.writeObject(deepPerson);
			System.out.println("Finished.");
		}
		System.out.println("Start reading:");
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("temp2.txt")))
		{
			DeepPerson deepPerson1 = (DeepPerson) ois.readObject();
			System.out.println(deepPerson1);
		}
	}
}
