package it.redhat.mrt;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class LocationTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/locations")
          .then()
             .statusCode(200);
    }

}