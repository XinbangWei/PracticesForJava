public class Doers
{
    public static void main(String[] args)
    {
        Emploee emp = new Emploee(Gender.Female);
        emp.SetAge(25);
        emp.Play();
        System.out.println(emp);
        emp.Sing();

        System.out.println();

        Manager mgr = new Manager(1);
        mgr.Play();
        System.out.println(mgr);
    }
}
enum Gender { Male, Female }
enum Education { Primary, Secondary, Tertiary }
abstract class Doer {
    private int _age;
    private Gender _gender;
    private Education _education;

    int GetAge() { return _age; }
    void SetAge(int age) { _age = age; }
    Gender GetGender() { return _gender; }
    void SetGender(Gender gender) { _gender = gender; }
    Education GetEducation() { return _education; }
    void SetEducation(Education education) { _education = education; }

    abstract void Play();
    public Doer(int age,Gender gender,Education education)
    {
        this._age = age;
        this._gender = gender;
        this._education = education;
    }
    public Doer(Gender gender)
    {
        this(18,gender,Education.Secondary);
    }
}
class Emploee extends Doer
{
    double _salary;
    double GetSalary() { return _salary; }
    void SetSalary(double salary) { _salary = salary; }
    private static int _employeeID = 0;
    private int _id;
    int GetID() { return _id; }

    @Override
    void Play()
    {
        System.out.println(new StringBuilder().append("Employee ID: ").append(GetID()).append(" is playing").toString());
    }

    public Emploee(int age,Gender gender,Education education,double salary)
    {
        super(age,gender,education);
        this._salary = salary;
        this._id = _employeeID++;
    }
    public Emploee(Gender gender)
    {
        this(18,gender,Education.Secondary,3000);
    }

    public final void Sing()
    {
        System.out.println(new StringBuilder().append("Employee ID: ").append(GetID()).append(" is singing").toString());
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ID: ").append(GetID())
                .append(", Age: ").append(GetAge())
                .append(", Gender: ").append(GetGender())
                .append(", Education: ").append(GetEducation())
                .append(", Salary: ").append(_salary).toString();
    }
}
class Manager extends Emploee
{
    final int Guru;
    Manager(int Guru)
    {
        this(30,Gender.Male,Education.Tertiary,5000,Guru);
    }
    Manager(int age,Gender gender,Education education,double salary,int Guru) {
        super(age, gender, education, salary);
        this.Guru = Guru;
    }

    @Override
    public String toString() {
        return super.toString() + new StringBuilder().append(", Guru: ").append(Guru).toString();
    }

    @Override
    void Play() {
        System.out.println(new StringBuilder().append("Manager ID: ").append(GetID()).append(" is playing").toString());
    }
}
