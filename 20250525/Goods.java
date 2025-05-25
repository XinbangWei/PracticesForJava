public class Goods {
    public static void main(String[] args)
    {
        Good[] goods = new Good[3];
        goods[0] = new Good(1, "Apple", 0.5, 10);
        goods[1] = new Good(2, "Banana", 0.3, 20);
        goods[2] = new Good(3, "Cherry", 0.2, 30);
        for(Good good : goods)
        {
            System.out.println(good);
        }
    }
}
class Good
{
    private int _id;
    private String _name;
    private double _price;
    private int _quantity;
    public Good(int id, String name, double price, int quantity)
    {
        _id = id;
        _name = name;
        _price = price;
        _quantity = quantity;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("ID: ").append(_id)
                .append(", Name: ").append(_name)
                .append(", Price: ").append(_price)
                .append(", Quantity: ").append(_quantity).toString();
    }
}
