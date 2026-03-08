package org.db.apicore.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.db.apicore.reporting.Reporter;

public class RestAssuredHandler {

    private int statusCode;
    private String statusDescription;
    private io.restassured.http.Headers responseHeaders;

    private Response response;

    public enum HttpMethod {
        GET {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.get(url);
            }
        },
        POST {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).post(url);
            }
        },
        PUT {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).put(url);
            }
        },
        DELETE {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.delete(url);
            }
        },
        PATCH {
            @Override
            public Response execute(RequestSpecification spec, String url, String body) {
                return spec.body(body).patch(url);
            }
        };

        public abstract Response execute(RequestSpecification spec, String url, String body);
    }


    /**
     * Executes an HTTP request using RestAssured and logs request + response.
     */
    public Response executeRequest(String method, String url, String requestBody) {
        try {

            Reporter.info("----- API RESPONSE -----");
            Reporter.info("Status Line: " + response.statusLine());
            Reporter.info("Headers: " + response.getHeaders());
            Reporter.info("Response Time: " + response.time() + " ms");
            Reporter.pass("API call completed successfully");

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
