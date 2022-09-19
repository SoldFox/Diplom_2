import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if(!user.getEmail().equals("") & !user.getPassword().equals("") & !user.getName().equals("")) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Проверяем создание пользователя с валидными данными")
    @Description("post /api/auth/register")
    public void userCanBeCreated() {
        user = UserGenerator.getDefault();
        ValidatableResponse response = userClient.createUser(user);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 200", 200, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertTrue("Value in \"success\" field = false", success);

        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать двух одинаковых пользователей")
    @Description("post /api/auth/register")
    public void cantCreateTwoIdenticalUsersTest() {
        user = UserGenerator.getDefault();
        accessToken = userClient.createUser(user).extract().path("accessToken");  // Creates first user and grabs access token
        ValidatableResponse response = userClient.createUser(user); // Creates second user

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 403", 403, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String actualBodyAnswer = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "User already exists", actualBodyAnswer);
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать пользователя без email")
    @Description("post /api/auth/register")
    public void cantCreateUserWithoutEmailTest() {
        user = UserGenerator.getUserWithEmptyEmail();
        ValidatableResponse response = userClient.createUser(user);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 403", 403, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String actualBodyAnswer = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "Email, password and name are required fields", actualBodyAnswer);
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать пользователя без password")
    @Description("post /api/auth/register")
    public void cantCreateUserWithoutPasswordTest() {
        user = UserGenerator.getUserWithEmptyPassword();
        ValidatableResponse response = userClient.createUser(user);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 403", 403, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String actualBodyAnswer = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "Email, password and name are required fields", actualBodyAnswer);
    }

    @Test
    @DisplayName("Проверяем, что нельзя создать пользователя без name")
    @Description("post /api/auth/register")
    public void cantCreateUserWithoutNameTest() {
        user = UserGenerator.getUserWithEmptyName();
        ValidatableResponse response = userClient.createUser(user);

        int statusCode = response.extract().statusCode();
        Assert.assertEquals("Status code is not 403", 403, statusCode);

        boolean success = response.extract().path("success");
        Assert.assertFalse("Value in \"success\" field = true", success);

        String actualBodyAnswer = response.extract().path("message");
        Assert.assertEquals("Text in \"message\" is incorrect", "Email, password and name are required fields", actualBodyAnswer);
    }
}