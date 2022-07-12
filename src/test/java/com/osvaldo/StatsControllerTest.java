package com.osvaldo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class StatsControllerTest {

    private static final String ENDPOINT = "/stats";
    private static final String RESPONSE_OK = "{\"count_human_dna\":4,\"count_mutant_dna\":6,\"ratio\":1.5}";

    @Test
    void testWhenResponseOK() {
        final String dnaPayload = "{}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(200)).body(containsString(RESPONSE_OK));
    }

}