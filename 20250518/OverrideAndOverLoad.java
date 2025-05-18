public class OverrideAndOverLoad {
    public static void main(String[] args)
    {
        System.out.println("Example of method overload:");
        System.out.println("1+2="+sum(1,2));
        System.out.println("1.5+2.5="+sum(1.5,2.5));
        System.out.println();
        System.out.print("Example of method override:");
        System.out.println("Base class:");
        Base base = new Base();
        base.Display();
        System.out.print("Derived class:");
        Base derived = new Derived();
        derived.Display();
    }
    public static int sum(int a, int b)
    {
        return a + b;
    }
    public static double sum(double a, double b)
    {
        return a + b;
    }
}
class Base
{
    public void Display()
        {
            System.out.println("Base class");
        }
}
class Derived extends Base
{
    public void Display()
        {
            System.out.println("Derived class");
        }
}
