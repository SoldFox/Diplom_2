import java.util.ArrayList;

public class OrderGenerator {

    private static String bunR2D3 = "61c0c5a71d1f82001bdaaa6d"; // Флюоресцентная булка R2-D3
    private static String meatImmortal = "61c0c5a71d1f82001bdaaa6f"; // Мясо бессмертных моллюсков Protostomia

    public static Order getDefault() {
        ArrayList<Object> order = new ArrayList<>();
        order.add(bunR2D3);
        order.add(meatImmortal);

        return new Order(order);
    }

    public static Order getOrderWithoutIngredients() {
        ArrayList<Object> order = new ArrayList<>();
        return new Order(order);
    }

    public static Order getOrderWithIncorrectHash() {
        ArrayList<Object> order = new ArrayList<>();
        order.add("abc123");
        order.add("666");

        return new Order(order);
    }
}