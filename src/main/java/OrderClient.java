import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{

    public static final String ORDER_URL = "/api/orders";

    @Step("Создание заказа без токена пользователя")
    public ValidatableResponse createOrderWithoutToken(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step("Создание заказа используя токен пользователя")
    public ValidatableResponse createOrder(String token, Order order) {
        return given()
                .spec(getBaseSpecToken(token))
                .body(order)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step("Получение данных о заказах пользователя")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .spec(getBaseSpecToken(token))
                .when()
                .get(ORDER_URL)
                .then();
    }
}