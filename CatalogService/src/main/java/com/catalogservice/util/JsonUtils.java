package com.catalogservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, String> jsonToMap(String json) {
        if (json == null || json.isEmpty()) {
            return Map.of();
        }
        try {
            return MAPPER.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON for specifications: " + json, e);
        }
    }

    public static List<String> jsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return MAPPER.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON for image URLs: " + json, e);
        }
    }

    public static String mapToJson(Map<String, String> map) {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize specifications: " + map, e);
        }
    }

    public static String listToJson(List<String> list) {
        try {
            return MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize image URLs: " + list, e);
        }
    }
}
