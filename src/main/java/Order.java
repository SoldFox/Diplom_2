import java.util.ArrayList;

public class Order extends RestClient{

    private ArrayList<Object> ingredients;

    public Order(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Object> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients=" + ingredients +
                '}';
    }
}