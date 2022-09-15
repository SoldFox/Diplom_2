import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class PatchUserTest {

    private User user;
    private UserClient userClient;
    private String accessToken;
    private final String newEmail = "tor1234@yandex.ru";
    private final String newPassword = "qwer";
    private final String newName = "lola";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @After
    public void tearDown(){
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Проверяем измнение данных пользователя (валиднымми данными)")
    @Description("patch /api/auth/user")
    public void userFieldsCanBePatchedTest() {
        userClient.createUser(user);
        accessToken = userClient.loginUser(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = userClient.patchUser(accessToken, new User(newEmail, newPassword, newName) );

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);

        Map<String, String> userList = response.extract().path("user");  // Put "user" hashMap into userList
        Assert.assertNotNull("The userList is null", userList);     // Checking that hashMap is not null
        Assert.assertEquals("User email during registration and after authorization do not match", newEmail, userList.get("email"));  // Checking that field "email" matches
        Assert.assertEquals("User name during registration and after authorization do not match", newName, userList.get("name"));     // Checking that field "name" matches
    }

    @Test
    @DisplayName("Проверяем, что нельзя измненить данные о пользователе без его авторизации")
    @Description("patch /api/auth/user")
    public void userFieldsCanBePatchedWithoutAuthorizationTest() {
        accessToken = userClient.createUser(user).extract().path("accessToken");
        ValidatableResponse response = userClient.patchUser(accessToken, new User(newEmail, newPassword, newName) );

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 401", 401, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String message = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "You should be authorised", message);
    }
}