package org.db.apicore.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.db.apicore.reporting.Reporter;

public class RestAssuredHandler {

    private int statusCode;
    private String statusDescription;
    private io.restassured.http.Headers responseHeaders;

    private Response response;

    /**
     * Executes an HTTP request using RestAssured and logs request + response.
     */
    public Response executeRequest(String method, String url, String requestBody) {
        try {

            // -------------------------
            // REQUEST LOGGING
            // -------------------------
            Reporter.info("----- API REQUEST -----");

            // -------------------------
            // EXECUTE REQUEST
            // -------------------------
            switch (method.toUpperCase()) {
                case "GET" ->
                        this.response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .log().all()
                                .get(url);

                case "POST" ->
                        this.response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .body(requestBody)
                                .log().all()
                                .post(url);

                case "PUT" ->
                        this.response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .body(requestBody)
                                .log().all()
                                .put(url);

                case "DELETE" ->
                        this.response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .log().all()
                                .delete(url);

                case "PATCH" ->
                        this.response = RestAssured
                                .given()
                                .contentType(ContentType.JSON)
                                .body(requestBody)
                                .log().all()
                                .patch(url);

                default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }

            // -------------------------
            // STORE RESPONSE DETAILS
            // -------------------------
            this.statusCode = response.statusCode();
            this.statusDescription = response.statusLine();
            this.responseHeaders = response.getHeaders();

            // -------------------------
            // RESPONSE LOGGING
            // -------------------------
            Reporter.info("----- API RESPONSE -----");
            Reporter.info("Status Line: " + statusDescription);
            Reporter.info("Headers: " + responseHeaders);
            Reporter.pass("API call completed successfully");

            return this.response;

        } catch (Exception e) {
            Reporter.fail("API request failed: " + e.getMessage());
            throw new RuntimeException("Error occurred while sending request", e);
        }
    }

    public String executeRequestAsString(String method, String url, String requestBody) {
        Response response = executeRequest(method, url, requestBody);
        return response.getBody().asString();
    }

    public String executeRequestAndGetJson(String method, String url, String requestBody, String jsonPath) {
        Response response = executeRequest(method, url, requestBody);
        return response.jsonPath().getString(jsonPath);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public io.restassured.http.Headers getResponseHeaders() {
        return responseHeaders;
    }

    public String getBody() {
        return response != null ? response.getBody().asString() : null;
    }

    public String getJsonValue(String path) {
        return response != null ? response.jsonPath().getString(path) : null;
    }
}
