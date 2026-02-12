package org.db.apicore.tests;

import org.db.apicore.core.QueryParametersHandler;
import org.db.apicore.core.RestAssuredHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParametersHandlerTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2/pet/findByStatus";

    @Test
    void testRemoveParameters_withRealPetstoreCall() {
        // Arrange
        String url = BASE_URL + "?status=available&limit=10&sort=asc";
        List<String> remove = List.of("limit", "sort");

        // Act
        String cleanedUrl = QueryParametersHandler.removeParameters(url, remove);

        // Assert URL correctness
        assertEquals(BASE_URL + "?status=available", cleanedUrl);

        // Call Petstore API
        RestAssuredHandler handler = new RestAssuredHandler();
        String response = handler.executeRequest("GET", cleanedUrl, null);

        // Assert API response
        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
    }

    @Test
    void testAddParameters_withRealPetstoreCall() {
        // Arrange
        String url = BASE_URL;
        List<String> params = List.of("status=available");

        // Act
        String finalUrl = QueryParametersHandler.addParameters(url, params);

        // Assert URL correctness
        assertEquals(BASE_URL + "?status=available", finalUrl);

        // Call Petstore API
        RestAssuredHandler handler = new RestAssuredHandler();
        String response = handler.executeRequest("GET", finalUrl, null);

        // Assert API response
        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
    }

    @Test
    void testAddAndRemoveParameters_withRealPetstoreCall() {
        // Arrange
        String url = BASE_URL;
        List<String> add = List.of("status=available", "limit=5", "sort=asc");

        // Add parameters
        String updatedUrl = QueryParametersHandler.addParameters(url, add);

        // Remove some
        List<String> remove = List.of("sort");
        String cleanedUrl = QueryParametersHandler.removeParameters(updatedUrl, remove);

        // Assert URL correctness
        assertEquals(BASE_URL + "?status=available&limit=5", cleanedUrl);

        // Call Petstore API
        RestAssuredHandler handler = new RestAssuredHandler();
        String response = handler.executeRequest("GET", cleanedUrl, null);

        // Assert API response
        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
    }

    @Test
    void testRemoveAllParameters_returnsBaseUrl_andStillValid() {
        // Arrange
        String url = BASE_URL + "?status=available";
        List<String> remove = List.of("status");

        // Act
        String cleanedUrl = QueryParametersHandler.removeParameters(url, remove);

        // Assert URL correctness
        assertEquals(BASE_URL, cleanedUrl);

        // Call Petstore API (Petstore returns 400 for missing required params)
        RestAssuredHandler handler = new RestAssuredHandler();
        handler.executeRequest("GET", cleanedUrl, null);

        // Assert API response
        assertTrue(handler.getStatusCode() == 400 || handler.getStatusCode() == 200);
    }
}
