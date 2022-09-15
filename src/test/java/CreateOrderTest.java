import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        userClient.createUser(user);
        accessToken = userClient.loginUser(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с валидными данными и используя токен пользователя")
    @Description("post /api/orders")
    public void createValidOrderWithToken() {
        order = OrderGenerator.getDefault();
        ValidatableResponse response = orderClient.createOrder(accessToken, order);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);

        int orderNumber = response.extract().path("order.number");
        Assert.assertNotEquals("Order number is equal 0",0, orderNumber);
    }

    @Test
    @DisplayName("Создание заказа без токена")
    @Description("post /api/orders")
    public void createValidOrderWithoutToken() {
        order = OrderGenerator.getDefault();
        ValidatableResponse response = orderClient.createOrderWithoutToken(order);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);

        HashMap<String, Object> body = response.extract().body().path("order");
        Assert.assertTrue(body.size() == 1 && body.containsKey("number"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("post /api/orders")
    public void createOrderWithoutIngredients() {
        order = OrderGenerator.getOrderWithoutIngredients();
        ValidatableResponse response = orderClient.createOrder(accessToken, order);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 400", 400, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String message = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиентов")
    @Description("post /api/orders")
    public void createOrderWithIncorrectIngredientsHash() {
        order = OrderGenerator.getOrderWithIncorrectHash();
        ValidatableResponse response = orderClient.createOrder(accessToken, order);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 500", 500, statusCode);
    }
}