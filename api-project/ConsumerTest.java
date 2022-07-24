package liveproject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
   //Header
    Map<String,String> Headers= new HashMap<>();
    //API path
    String userpath = "/api/users";

    @Pact(consumer="userConsumer", provider = "userProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder)
    {
    //set headers
        Headers.put("Content-Type","application/json");
        //set body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");
        //create pact
        return builder.given( " A request to craete user")
                .uponReceiving(" A request to craete user")
                .method("POST")
                .headers(Headers)
                .path(userpath)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "userProvider",port ="8282")
    public void consumerSideTest(){
        final String baseURI = " http://localhost:8282";
    //Create Request Body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id",1);
        reqBody.put("firstName","Renuka");
        reqBody.put("lastName","Dimble");
        reqBody.put("email","Renuka@example.com");

        //Generate Response
        Response response=given().headers(Headers).when().body(reqBody).post(baseURI + userpath);
        System.out.println(response.getBody().asPrettyString());

        //Assertions
        response.then().statusCode(201);

    }
}
