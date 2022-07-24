package liveproject;


import static io.restassured.RestAssured.given;

        import org.testng.annotations.BeforeClass;
        import org.testng.annotations.Test;

        import io.restassured.builder.RequestSpecBuilder;
        import io.restassured.http.ContentType;
        import io.restassured.response.Response;
        import io.restassured.specification.RequestSpecification;
        import io.restassured.specification.ResponseSpecification;

public class GitHubProject {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    final String baseURI = "https://api.github.com/user/keys";
    String SSHKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQD27y12UHmD3181IFzuc72HkfnemV2/Br85t2axMiW2z/8AokQGZSMFHr3sx1Siy/7o2NfXRjkKw6/7zCFSdL19AXZb/RrIXTcsmJjuDrwi6RY8S7heU3bNfUg9+n5NFoMhqnUKzzJSlRohMWDsYSVmLOZf+WOYRuRCjDZYmD2me3XJ2Npixo/1vq1kZnAYcipssO4q2DxcMCgRq6iMeNsHMxrK5LQqsAhE8C7H1cH5xKKtJYHvkBEw/8jztpBxZp7WGRZjJf4l8VM5YCJBP/bF7BwdOXkN9KN+ebf5XctVLovDZrlbRHP2Wv5czOHUHxEePprQhuA3iaP97vgeSfyh  ";
    String Tokenid = "token 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";
    int id;

    @BeforeClass
    public void setup() {
        requestSpecification = new RequestSpecBuilder().
                addHeader("Authorization", Tokenid).
                setBaseUri(baseURI)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test(priority = 1)
    public void addSSHKey() {
        String reqBody = "{" + "\"title\":\"TestAPIKey\"," + "\"key\":\"" + SSHKey + "\"" + "}";

        Response response = given().spec(requestSpecification).body(reqBody).when().post();
        System.out.println(response.prettyPrint());
        id = response.then().extract().path("id");
        response.then().statusCode(201);

    }

    @Test(priority = 2)
    public void getSSHKey() {

        Response response = given().spec(requestSpecification).when().
                pathParam("keyid", id) // Set path parameter
                .get(baseURI + "/{keyid}");

        System.out.println(response.getBody().asPrettyString());

        response.then().statusCode(200);
    }

    @Test(priority = 3)

    public void deleteSSHKey() {

        Response response = given().spec(requestSpecification).when().
                pathParam("keyid", id) // Set path parameter
                .delete(baseURI + "/{keyid}"); // Send DELETE request

        System.out.println(response.getBody().asPrettyString());
        // Assertion
        response.then().statusCode(204);

    }
}


