package org.db.apicore.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

public class JsonUtility {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parse JSON string into JsonNode.
     */
    public static JsonNode parse(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON input", e);
        }
    }

    /**
     * Convert JsonNode back to JSON string.
     */
    public static String toJson(JsonNode node) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize JSON", e);
        }
    }

    /**
     * Update JSON using a key path like "user.address.city"
     */
    public static JsonNode update(JsonNode root, String path, String newValue) {
        String[] keys = path.split("\\.");

        JsonNode current = root;
        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];

            if (key.contains("[")) {
                // array index
                String field = key.substring(0, key.indexOf("["));
                int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
                current = current.get(field).get(index);
            } else {
                current = current.get(key);
            }
        }

        String lastKey = keys[keys.length - 1];

        if (lastKey.contains("[")) {
            String field = lastKey.substring(0, lastKey.indexOf("["));
            int index = Integer.parseInt(lastKey.substring(lastKey.indexOf("[") + 1, lastKey.indexOf("]")));

            ArrayNode array = (ArrayNode) current.get(field);
            array.set(index, mapper.convertValue(newValue, JsonNode.class));
        } else {
            ((ObjectNode) current).put(lastKey, newValue);
        }

        return root;
    }

    /**
     * Remove a field using a key path like "user.address.city"
     */
    public static JsonNode remove(JsonNode root, String path) {
        String[] keys = path.split("\\.");

        JsonNode current = root;
        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];

            if (key.contains("[")) {
                String field = key.substring(0, key.indexOf("["));
                int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
                current = current.get(field).get(index);
            } else {
                current = current.get(key);
            }
        }

        String lastKey = keys[keys.length - 1];

        if (lastKey.contains("[")) {
            String field = lastKey.substring(0, lastKey.indexOf("["));
            int index = Integer.parseInt(lastKey.substring(lastKey.indexOf("[") + 1, lastKey.indexOf("]")));

            ArrayNode array = (ArrayNode) current.get(field);
            array.remove(index);
        } else {
            ((ObjectNode) current).remove(lastKey);
        }

        return root;
    }
}
