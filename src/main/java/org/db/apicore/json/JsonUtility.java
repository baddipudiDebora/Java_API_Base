package org.db.apicore.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.db.apicore.reporting.Reporter;

public class JsonUtility {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parse JSON string into JsonNode.
     */
    public static JsonNode parse(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            Reporter.fail("Invalid JSON input: " + e.getMessage());
            System.out.println("[JSON ERROR] Invalid JSON input: " + e.getMessage());
            throw new RuntimeException("Invalid JSON input", e);
        }
    }

    /**
     * Convert JsonNode back to JSON string.
     * Logs the final payload once.
     */
    public static String toJson(JsonNode node) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

            // Reporter log
            Reporter.info("Final JSON payload:\n" + json);

            // Console log
            System.out.println("----- Final JSON Payload -----");
            System.out.println(json);
            System.out.println("------------------------------");

            return json;

        } catch (Exception e) {
            Reporter.fail("Unable to serialize JSON: " + e.getMessage());
            System.out.println("[JSON ERROR] Unable to serialize JSON: " + e.getMessage());
            throw new RuntimeException("Unable to serialize JSON", e);
        }
    }

    /**
     * Update JSON using a key path like "user.address.city" or "items[0].name".
     * Logs only the key + value pair.
     */
    public static JsonNode update(JsonNode root, String path, String newValue) {

        // Reporter log
        Reporter.info("Updated field: " + path + " = " + newValue);

        // Console log
        System.out.println("[JSON UPDATE] " + path + " = " + newValue);

        return update(root, path, mapper.convertValue(newValue, JsonNode.class));
    }

    /**
     * Internal update method supporting any JsonNode type.
     */
    private static JsonNode update(JsonNode root, String path, JsonNode newValue) {
        if (!(root instanceof ObjectNode objectRoot)) {
            Reporter.fail("Root JSON must be an ObjectNode");
            System.out.println("[JSON ERROR] Root JSON must be an ObjectNode");
            throw new IllegalArgumentException("Root JSON must be an ObjectNode");
        }

        String[] keys = path.split("\\.");
        JsonNode current = objectRoot;

        for (int i = 0; i < keys.length - 1; i++) {
            current = navigateOrCreate(current, keys[i]);
        }

        applyValue(current, keys[keys.length - 1], newValue);
        return root;
    }

    /**
     * Remove a field using a key path.
     */
    public static JsonNode remove(JsonNode root, String path) {

        // Reporter log
        Reporter.info("Removed field: " + path);

        // Console log
        System.out.println("[JSON REMOVE] " + path);

        if (!(root instanceof ObjectNode objectRoot)) {
            Reporter.fail("Root JSON must be an ObjectNode");
            System.out.println("[JSON ERROR] Root JSON must be an ObjectNode");
            throw new IllegalArgumentException("Root JSON must be an ObjectNode");
        }

        String[] keys = path.split("\\.");
        JsonNode current = objectRoot;

        for (int i = 0; i < keys.length - 1; i++) {
            current = navigate(current, keys[i]);
            if (current == null) return root;
        }

        removeValue(current, keys[keys.length - 1]);
        return root;
    }

    // -------------------------
    // INTERNAL HELPERS (NO LOGGING)
    // -------------------------

    private static JsonNode navigate(JsonNode node, String key) {
        if (key.contains("[")) {
            String field = key.substring(0, key.indexOf("["));
            int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

            JsonNode arrayNode = node.get(field);
            if (arrayNode == null || !arrayNode.isArray()) return null;

            return arrayNode.get(index);
        }
        return node.get(key);
    }

    private static JsonNode navigateOrCreate(JsonNode node, String key) {
        if (!(node instanceof ObjectNode objectNode)) {
            throw new IllegalArgumentException("Cannot navigate non-object node: " + node);
        }

        if (key.contains("[")) {
            String field = key.substring(0, key.indexOf("["));
            int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

            ArrayNode array = ensureArray(objectNode, field);
            return ensureArrayIndex(array, index);

        } else {
            JsonNode child = objectNode.get(key);
            if (child == null || child.isNull()) {
                child = objectNode.putObject(key);
            }
            return child;
        }
    }

    private static ArrayNode ensureArray(ObjectNode parent, String field) {
        JsonNode existing = parent.get(field);

        if (existing == null || !existing.isArray()) {
            existing = parent.putArray(field);
        }

        return (ArrayNode) existing;
    }

    private static JsonNode ensureArrayIndex(ArrayNode array, int index) {
        while (array.size() <= index) {
            array.addObject();
        }
        return array.get(index);
    }

    private static void applyValue(JsonNode current, String key, JsonNode value) {
        if (!(current instanceof ObjectNode objectNode)) {
            throw new IllegalArgumentException("Cannot set value on non-object node: " + current);
        }

        if (key.contains("[")) {
            String field = key.substring(0, key.indexOf("["));
            int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

            ArrayNode array = ensureArray(objectNode, field);
            ensureArrayIndex(array, index);
            array.set(index, value);

        } else {
            objectNode.set(key, value);
        }
    }

    private static void removeValue(JsonNode current, String key) {
        if (!(current instanceof ObjectNode objectNode)) return;

        if (key.contains("[")) {
            String field = key.substring(0, key.indexOf("["));
            int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

            JsonNode arr = objectNode.get(field);
            if (arr instanceof ArrayNode arrayNode && index < arrayNode.size()) {
                arrayNode.remove(index);
            }

        } else {
            objectNode.remove(key);
        }
    }
}
