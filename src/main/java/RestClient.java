import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    public RequestSpecification getBaseSpecToken(String token) {
        String validToken = token.replaceAll("(Bearer )", "");
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + validToken)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}