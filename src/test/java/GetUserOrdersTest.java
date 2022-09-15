import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class GetUserOrdersTest {

    private User user;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        userClient.createUser(user);
        accessToken = userClient.loginUser(UserCredentials.from(user)).extract().path("accessToken");
        order = OrderGenerator.getDefault();
        orderClient.createOrder(accessToken, order);
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Проверяем, что можно получить данные о заказах пользователя (используя валидный токен)")
    @Description("get /api/orders")
    public void getOrdersFromAuthUser() {
        ValidatableResponse response = orderClient.getUserOrders(accessToken);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);

        List<Object> body = response.extract().path("orders");
        Assert.assertFalse("Body is empty", body.isEmpty());
    }

    @Test
    @DisplayName("Проверяем получение данных о заказах пользователя используя пустой токен")
    @Description("get /api/orders")
    public void getOrdersWithoutToken() {
        String emptyToken = "";
        ValidatableResponse response = orderClient.getUserOrders(emptyToken);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 401", 401, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String message = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "You should be authorised", message);
    }
}