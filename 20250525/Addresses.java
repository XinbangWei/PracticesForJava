import java.io.Serializable;

public class Addresses
{
	public static void main(String[] args)
	{
		Address homeAddress = new Address("New York", "NY", "USA");
		Address workAddress = new Address("Los Angeles", "CA", "USA");
		Employee employee = new Employee("John Doe", "JD@JD.com", homeAddress, workAddress);
		System.out.println(employee);
	}
}
class Employee
{
	String _name;
	String _email;
	Address _homeAddress;
	Address _workAddress;
	Employee(String name, String email, Address homeAddress, Address workAddress)
	{
		this._name = name;
		this._email = email;
		this._homeAddress = homeAddress;
		this._workAddress = workAddress;
	}
	String GetName() { return _name; }
	String GetEmail() { return _email; }
	Address GetHomeAddress() { return _homeAddress; }
	Address GetWorkAddress() { return _workAddress; }
	@Override
	public String toString()
	{
		return new StringBuilder().append("Name: ").append(_name)
				.append("\nEmail: ").append(_email)
				.append("\nHome Address: ").append(_homeAddress)
				.append("\nWork Address: ").append(_workAddress).toString();
	}
}
class Address implements Serializable
{
	String _city;
	String _province;
	String _country;
	Address(String city, String province, String country)
	{
		this._city = city;
		this._province = province;
		this._country = country;
	}
	String GetCity() { return _city; }
	String GetProvince() { return _province; }
	String GetCountry() { return _country; }
	@Override
	public String toString()
	{
		return new StringBuilder().append("City: ").append(_city)
				.append(", Province: ").append(_province)
				.append(", Country: ").append(_country).toString();
	}
}