// ABOUTME: Reusable REST API helper methods wrapping REST Assured operations.
// ABOUTME: Provides authenticated GET, POST, PUT, DELETE methods returning Response objects.

package com.capstone.utils;

import com.capstone.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ApiHelper {

    private ApiHelper() {
    }

    public static Response get(String endpoint) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .when()
                .get(endpoint);
    }

    public static Response getById(String endpoint, int id) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .when()
                .get(endpoint + "/" + id);
    }

    public static Response post(String endpoint, Object body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response put(String endpoint, int id, Object body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(endpoint + "/" + id);
    }

    public static Response delete(String endpoint, int id) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .when()
                .delete(endpoint + "/" + id);
    }
}
