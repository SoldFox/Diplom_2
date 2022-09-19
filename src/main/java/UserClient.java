import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    public static final String USER_URL = "/api/auth";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_URL + "/register")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(USER_URL + "/login")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseSpecToken(token))
                .when()
                .delete(USER_URL + "/user")
                .then().log().body();
    }

    @Step("Обновление данных о пользователе")
    public ValidatableResponse patchUser(String token, User user) {
        return given()
                .spec(getBaseSpecToken(token))
                .body(user)
                .when()
                .patch(USER_URL + "/user")
                .then();
    }
}