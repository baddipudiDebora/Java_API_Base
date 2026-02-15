package org.db.apicore.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestAssuredHandler {

    private int statusCode;
    private String statusDescription;
    private io.restassured.http.Headers responseHeaders;

    // ✅ NEW: store response
    private Response response;

    /**
     * Executes an HTTP request using RestAssured and returns the response.
     */
    public Response executeRequest(String method, String url, String requestBody) {
        try {

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

            // store response details
            this.statusCode = response.statusCode();
            this.statusDescription = response.statusLine();
            this.responseHeaders = response.getHeaders();

            return this.response;

        } catch (Exception e) {
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

    // NEW: get full body as string
    public String getBody() {
        return response != null ? response.getBody().asString() : null;
    }

    //  NEW: get JSON value by path
    public String getJsonValue(String path) {
        return response != null ? response.jsonPath().getString(path) : null;
    }
}
