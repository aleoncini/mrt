package it.redhat.mrt;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ReportTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/reports")
          .then()
             .statusCode(200)
             .body(is("ok\n"));
    }

}