package com.osvaldo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GeneticControllerTest {

    final static String ENDPOINT = "/mutant";

    @Test
    void testRequestWhenOKStatus() {

        final String dnaPayload = "{\"dna\":[\"ATGCGA\", \"CAGTGC\", \"TTATGT\", \"AGAAGG\", \"CCCCTA\", \"TCACTG\"]}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(200));
    }

    @Test
    void testResponse403_WhenDNAFieldDoesNotExist() {

        final String dnaPayload = "{}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(403));

    }

    @Test
    void testResponse403_WhenDNAFieldNotValidType() {

        final String dnaPayload = "{\"dna\":1}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(403));

    }

    @Test
    void testResponse403_WhenDNAMatrixHighNotBelongWidth() {

        final String dnaPayload = "{\"dna\":[\"ACGTC\", \"ACTGC\", \"GCTAC\", \"TGACC\",\"TGAGAT\"]}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(403));
    }

    @Test
    void testResponse403_WhenDNAMatrixNotBeEmpty() {

        final String dnaPayload = "{\"dna\":[]}";
        given().header("Content-Type", MediaType.APPLICATION_JSON)
                .urlEncodingEnabled(false)
                .request()
                .body(dnaPayload)
                .when().post(ENDPOINT)
                .then().statusCode(is(403));
    }

}