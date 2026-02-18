package org.db.apicore.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilityDynamicTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCreateMissingNestedObjects() throws Exception {
        JsonNode json = mapper.readTree("{}");

        JsonUtility.update(json, "user.address.city", "Toronto");

        assertEquals("Toronto",
                json.get("user").get("address").get("city").asText());
    }

    @Test
    void testCreateArrayAndIndexAutomatically() throws Exception {
        JsonNode json = mapper.readTree("{}");

        JsonUtility.update(json, "items[2].name", "Book");

        assertEquals("Book",
                json.get("items").get(2).get("name").asText());
    }

    @Test
    void testUpdateArrayIndexValue() throws Exception {
        JsonNode json = mapper.readTree("{\"items\": [{\"name\": \"A\"}]}");

        JsonUtility.update(json, "items[0].name", "Updated");

        assertEquals("Updated", json.get("items").get(0).get("name").asText());
    }

    @Test
    void testAppendLikeBehaviorBySettingNextIndex() throws Exception {
        JsonNode json = mapper.readTree("{\"items\": []}");

        JsonUtility.update(json, "items[0].value", "X");
        JsonUtility.update(json, "items[1].value", "Y");

        assertEquals("X", json.get("items").get(0).get("value").asText());
        assertEquals("Y", json.get("items").get(1).get("value").asText());
    }

    @Test
    void testRemoveNestedField() throws Exception {
        JsonNode json = mapper.readTree("{\"user\": {\"address\": {\"city\": \"Toronto\"}}}");

        JsonUtility.remove(json, "user.address.city");

        assertFalse(json.get("user").get("address").has("city"));
    }

    @Test
    void testRemoveArrayIndex() throws Exception {
        JsonNode json = mapper.readTree("{\"items\": [\"A\", \"B\", \"C\"]}");

        JsonUtility.remove(json, "items[1]");

        assertEquals(2, json.get("items").size());
        assertEquals("C", json.get("items").get(1).asText());
    }

    @Test
    void testRemoveNonExistingPathDoesNotThrow() throws Exception {
        JsonNode json = mapper.readTree("{\"user\": {\"name\": \"Debora\"}}");

        JsonUtility.remove(json, "user.address.city");

        assertTrue(json.get("user").has("name"));
    }

    @Test
    void testUpdateDeepMixedPath() throws Exception {
        JsonNode json = mapper.readTree("{}");

        JsonUtility.update(json, "a.b[1].c[2].d", "value");

        assertEquals("value",
                json.get("a").get("b").get(1).get("c").get(2).get("d").asText());
    }
}
