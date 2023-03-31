package com.sdui.builder.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ArraySelectPathfinder {
    
    private static Matcher ARRAY_SELECT_PATHFINDER_MATCHER = Pattern.compile("(?<origin>(.*?))(?<array>\\[(?<selector>.*?)==(?<matcher>.*?)\\])").matcher("");
    private final String ORIGIN_STRING = "origin";
    private final String ARRAY_STRING = "array";
    private final String SELECTOR_STRING = "selector";
    private final String MATCHER_STRING = "matcher";

    String path;

    public ArraySelectPathfinder(String path) {
        super();
        this.path = path;
    }

    private Boolean isArraySelectPath(String path) {
        ARRAY_SELECT_PATHFINDER_MATCHER.reset(path);
        return ARRAY_SELECT_PATHFINDER_MATCHER.find();
    }

    public String calculatePath(JsonNode node) {
        if (!isArraySelectPath(path)) return null;
        String originPath = ARRAY_SELECT_PATHFINDER_MATCHER.group(ORIGIN_STRING);
        String selectorPath = ARRAY_SELECT_PATHFINDER_MATCHER.group(SELECTOR_STRING);

        JsonNode originNode = node.at(originPath);
        if (!originNode.isArray()) return null;
        ArrayNode arrayNode = (ArrayNode) originNode;

        Integer index = findIndex(selectorPath, arrayNode);
        return newPath(index);
    }

    private Integer findIndex(String selectorPath, ArrayNode arrayNode) {
        String matcherPath = ARRAY_SELECT_PATHFINDER_MATCHER.group(MATCHER_STRING).trim();
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode nodeMatch = arrayNode.get(i).at(selectorPath.trim());
            if (nodeMatch.isTextual()) {
                String textToMatch = nodeMatch.asText();
                if (textToMatch.equals(matcherPath)) {
                    return i;
                }
            } else if (nodeMatch.isInt()) {
                Integer numberToMatch = nodeMatch.asInt();
                if (numberToMatch.equals(Integer.parseInt(matcherPath))) {
                    return i;
                }
            }
        }
        return null;
    }

    private String newPath(Integer index) {
        if (index == null) return null;
        StringBuilder builder = new StringBuilder(path);
        Integer startMatch = ARRAY_SELECT_PATHFINDER_MATCHER.start(ARRAY_STRING);
        Integer endMatch = ARRAY_SELECT_PATHFINDER_MATCHER.end(ARRAY_STRING);
        builder.replace(startMatch, endMatch, String.valueOf(index));
        builder.insert(startMatch, "/");
        return builder.toString();
    }
}
