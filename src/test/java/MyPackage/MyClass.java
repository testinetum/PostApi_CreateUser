package MyPackage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MyClass {
	private static final Logger logger = Logger.getLogger(MyClass.class.getName());
	
    private String requestBody;
    private Response response;
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    
    @Given("The user data is read from {string}")
    public void The_user_data_is_read_from(String filePath) throws IOException {
    	logger.info("Reading user data from file: " + filePath);
        try {
            requestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)));
            logger.info("Successfully read user data.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading user data from file: " + filePath, e);
            throw e;
        }
    }
    
    @When("I send a post request to {string}")
    public void I_send_a_post_request_to(String endpoint) {
    	logger.info("Sending POST request to endpoint: " + endpoint);
        RequestSpecification request = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(requestBody);
        response = request.post(endpoint);
        logger.info("Received response with status code: " + response.getStatusCode());
    }
    
    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer status) {
    	 logger.info("Checking if response status is: " + status);
         assertThat(response.statusCode(), equalTo(status));
         logger.info("Response status validation passed.");
     }
    
    
    @Then("The response body matches the reference from {string}")
    public void The_response_body_matches_the_reference_from(String filePath) throws IOException {
    	
logger.info("Comparing response body with reference file: " + filePath);
        
        try {
            // Lire le contenu du fichier attendu
            String expectedResponseString = new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)));
            JSONObject expectedResponseJson = new JSONObject(expectedResponseString);
            logger.info("Expected response: " + expectedResponseJson.toString());

            // Convertir la réponse API en JSON
            JSONObject actualResponseJson = new JSONObject(response.getBody().asString());
            logger.info("Actual response: " + actualResponseJson.toString());

            // Comparer les deux objets JSON
            assertThat(actualResponseJson.toString(), is(expectedResponseJson.toString()));
            logger.info("Response body validation passed.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading reference file: " + filePath, e);
            throw e;
        }
    }
}