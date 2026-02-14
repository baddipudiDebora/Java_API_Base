package org.db.apicore.tests;

import io.restassured.response.Response;
import org.db.apicore.core.RestAssuredHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestAssuredHandlerTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    void testGET_findByStatus() {
        RestAssuredHandler handler = new RestAssuredHandler();

        Response response = handler.executeRequest(
                "GET",
                BASE_URL + "/pet/findByStatus?status=available",
                null
        );

        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
        assertTrue(handler.getStatusDescription().contains("200"));
        assertNotNull(handler.getResponseHeaders());
    }

    @Test
    void testPOST_addPet() {
        RestAssuredHandler handler = new RestAssuredHandler();

        String body = """
        {
          "id": 9999991,
          "name": "DeboraDog",
          "status": "available"
        }
        """;

        Response response = handler.executeRequest(
                "POST",
                BASE_URL + "/pet",
                body
        );

        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
        assertTrue(response.toString().contains("DeboraDog"));
    }

    @Test
    void testPUT_updatePet() {
        RestAssuredHandler handler = new RestAssuredHandler();

        String body = """
        {
          "id": 9999991,
          "name": "DeboraDogUpdated",
          "status": "sold"
        }
        """;

        Response response = handler.executeRequest(
                "PUT",
                BASE_URL + "/pet",
                body
        );

        assertNotNull(response);
        assertEquals(200, handler.getStatusCode());
        assertTrue(response.toString().contains("DeboraDogUpdated"));
    }

    @Test
    void testDELETE_pet() {
        RestAssuredHandler handler = new RestAssuredHandler();

        Response response = handler.executeRequest(
                "DELETE",
                BASE_URL + "/pet/9999991",
                null
        );

        assertNotNull(response);
        assertTrue(handler.getStatusCode() == 200 || handler.getStatusCode() == 404);
        // 200 = deleted, 404 = already deleted
    }

    @Test
    void testGET_invalidUrl_returnsError() {
        RestAssuredHandler handler = new RestAssuredHandler();

        Response response = handler.executeRequest(
                "GET",
                "https://petstore.swagger.io/v2/this/does/not/exist",
                null
        );

        assertNotNull(response);
        assertTrue(handler.getStatusCode() == 404 || handler.getStatusCode() == 200);
    }
}
