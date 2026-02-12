package org.db.apicore.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestAssuredHandler {

    private int statusCode;
    private String statusDescription;
    private io.restassured.http.Headers responseHeaders;

    /**
     * Executes an HTTP request using RestAssured and returns the response body.
     */
    public String executeRequest(String method, String url, String requestBody) {

        Response response;

        try {
            switch (method.toUpperCase()) {
                case "GET" -> response = RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .log().all()
                        .get(url);

                case "POST" -> response = RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .log().all()
                        .post(url);

                case "PUT" -> response = RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .log().all()
                        .put(url);

                case "DELETE" -> response = RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .log().all()
                        .delete(url);

                default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }

            this.statusCode = response.statusCode();
            this.statusDescription = response.statusLine();
            this.responseHeaders = response.getHeaders();

            return response.asString();

        } catch (Exception e) {
            this.statusDescription = "Error occurred while sending request";
            return e.getMessage();
        }
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
}
