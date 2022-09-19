import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        accessToken = userClient.createUser(user).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);  // deleting user
    }

    @Test
    @DisplayName("Проверяем авторизацию пользователя с валидными данными")
    @Description("post /api/auth/login")
    public void userCanLoginTest() {
        ValidatableResponse response = userClient.loginUser(UserCredentials.from(user));

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);  // Check status code

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);  // Check boolean value in field "success"

        accessToken = response.extract().path("accessToken");
        Assert.assertNotNull("The value in \"accessToken\" is null", accessToken);  // Checking that access token is not null

        String refreshToken = response.extract().path("refreshToken");
        Assert.assertNotNull("The value in \"refreshToken\" is null", refreshToken);  // Checking that refresh token is not null

        Map<String, String> userList = response.extract().path("user");  // Put "user" hashMap into userList
        Assert.assertNotNull("The userList is null", userList);     // Checking that hashMap is not null
        Assert.assertEquals("User email during registration and after authorization do not match", user.getEmail().toLowerCase(), userList.get("email"));  // Checking that field "email" matches
        Assert.assertEquals("User name during registration and after authorization do not match", user.getName(), userList.get("name"));                  // Checking that field "name" matches
    }

    @Test
    @DisplayName("Проверяем, что нельзя авторизоваться с неверным email пользователя")
    @Description("post /api/auth/login")
    public void userCantLoginWithWrongEmailTest() {
        ValidatableResponse response = userClient.loginUser(new UserCredentials("BabaVasya321@yandex.ru", user.getPassword()));

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 401", 401, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String message = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "email or password are incorrect", message);
    }

    @Test
    @DisplayName("Проверяем, что нельзя авторизоваться с неверным password пользователя")
    @Description("post /api/auth/login")
    public void userCantLoginWithWrongPasswordTest() {
        ValidatableResponse response = userClient.loginUser(new UserCredentials(user.getEmail(), "qwer123"));

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 401", 401, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String message = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "email or password are incorrect", message);
    }
}