package org.db.apicore.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.db.apicore.reporting.Reporter;
import org.db.apicore.utils.ApiLogger;

public class RestAssuredHandler {

    private int statusCode;
    private String statusDescription;
    private io.restassured.http.Headers responseHeaders;

    private Response response;

    public enum HttpMethod {
        GET {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.when().get(url);
            }
        },
        POST {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).when().post(url);
            }
        },
        PUT {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).when().put(url);
            }
        },
        DELETE {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.when().delete(url);
            }
        },
        PATCH {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).when().patch(url);
            }
        };

        public abstract Response execute(RequestSpecification spec, String url, String body);
    }

    /**
     * Executes an HTTP request using RestAssured and logs request + response.
     */
    public Response executeRequest(String method, String url, String requestBody) {
        try {
            // Log request
            ApiLogger.logRequest(method, url, requestBody);

            // Build request spec
            RequestSpecification spec = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log().all();

            // Convert method string to enum
            HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());

            // Execute request
            this.response = httpMethod.execute(spec, url, requestBody);

            // Capture response details
            this.statusCode = response.statusCode();
            this.statusDescription = response.statusLine();
            this.responseHeaders = response.getHeaders();

            // Log response
            ApiLogger.logresponse(response);

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
