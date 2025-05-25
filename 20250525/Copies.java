import java.io.*;

public class Copies
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Before change:");
		Address address1 = new Address("New York", "NY", "USA");
		Address address2 = new Address("Los Angeles", "CA", "USA");
		ShadowPerson person1 = new ShadowPerson("John", 30,address1);
		ShadowPerson person2 = (ShadowPerson) person1.clone();
		DeepPerson person3 = new DeepPerson("Jane", 25,address2);
		DeepPerson person4 = (DeepPerson) person3.clone();
		System.out.println("person1: " + person1);
		System.out.println("person2: " + person2);
		System.out.println("person3: " + person3);
		System.out.println("person4: " + person4);
		System.out.println();

		person1.Age = 35;
		person1.Name = "Mike";
		person1.address._city = "Changed City";
		person1.address._country = "Changed Country";
		person1.address._province = "Changed Province";

		person3.Age = 28;
		person3.Name = "Alice";
		person3.address._city = "Changed City2";
		person3.address._country = "Changed Country2";
		person3.address._province = "Changed Province2";

		System.out.println("After change:");
		System.out.println("person1: " + person1);
		System.out.println("person2: " + person2);
		System.out.println("person3: " + person3);
		System.out.println("person4: " + person4);
	}
}

class DeepPerson implements Cloneable, Serializable
{
	String Name;
	int Age;
	Address address;

	DeepPerson(String name, int age,Address address)
	{
		this.Name = name;
		this.Age = age;
		this.address = address;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);) {
			oos.writeObject(this);
			try (
					ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
					ObjectInputStream ois = new ObjectInputStream(bais)
			) {
				return ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new CloneNotSupportedException(e.getMessage());
		}
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append("Name: ").append(Name)
				.append(", Age: ").append(Age).append("\nAddress:").append(address).toString();
	}
}
class ShadowPerson implements Cloneable
{
	String Name;
	int Age;
	Address address;
	ShadowPerson(String name, int age,Address address)
	{
		this.Name = name;
		this.Age = age;
		this.address = address;
	}
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append("Name: ").append(Name)
				.append(", Age: ").append(Age).append("\nAddress:").append(address).toString();
	}
}
