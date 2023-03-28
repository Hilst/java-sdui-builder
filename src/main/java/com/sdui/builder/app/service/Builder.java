package com.sdui.builder.app.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sdui.builder.app.model.layout.Content;
import com.sdui.builder.app.model.layout.Layout;
import com.sdui.builder.app.model.layout.Page;
import com.sdui.builder.app.model.layout.Section;
import com.sdui.builder.app.utils.ArraySelector;
import com.sdui.builder.app.utils.Path;

/**
 * IN LAYOUT (OBJECT PARSED) AND DATA (JSON NODE NOT PARSABLE BUSINESS)
 * OUT LAYOUT FOLLOWING RULES
 * 
 * RULES:
 * BASIC:
 * FROM: data.<path> in value layout => substitute for stringfy(<object>) in <path>
 * 
 * ARRAY PARSING:
 * 1 IF: <path> is invlaid => remove content
 * 2 IF: <path> contains ".<integer>." => access item of the list to continue path
 * 3 IF: <path> contains "[<subpath> == <value>].<subpath>" => check for first item that satisfies and continue <subpath> from it
 * 
 * EMPTY RULE: 
 * 4 IF: page has empty sections => remove page
 * 5 IF: section has empty content => remove section
 * 6 IF: data.<path> null or non existent => remove content
 * 7 IF: blank value => remove content
 * 8 IF: value don't conform to data.<path> => return value
 * */ 

@Service
public class Builder {
    
    public Layout buildLayoutWithData(Layout layout, JsonNode data) {
        layout.setPages(buildPages(layout.getPages(), data));     
        return layout;
    }

    private List<Page> buildPages(List<Page> pages, JsonNode data) {
        List<Page> newPages = new ArrayList<>();

        for (Page page : pages) {
            page.setSections(buildSections(page.getSections(), data));
        
            if (!page.getSections().isEmpty()) { // COND: 4
                newPages.add(page);
            }
        }

        return newPages;
    }

    private List<Section> buildSections(List<Section> sections, JsonNode data) {
        List<Section> newSections = new ArrayList<>();
        
        for (Section section : sections) {
            section.setContents(buildContents(section.getContents(), data));
        
            if (!section.getContents().isEmpty()) { // COND: 5
                newSections.add(section);
            }
        }
        
        return newSections;
    }

    private List<Content> buildContents(List<Content> contents, JsonNode data) {
        List<Content> newContents = new ArrayList<>();
        
        for (Content content : contents) {

            String value = null;
            if (content.getValue().contains("data.")) { // COND: 8
                value = buildValue(content.getValue(), data, false);
            } else if (!content.getValue().isBlank()) { // COND: 7
                value = content.getValue();
            }

            if (value != null) { // COND: 6
                content.setValue(value);
                newContents.add(content);
            }
        }
        
        return newContents;
    }

    private String buildValue(String value, JsonNode data, Boolean fromNode) {

        LinkedList<Path> paths = pathsFromValue(value);
        JsonNode node = data;

        for (Path path : paths) {

            // START NULLABLES
            Integer integer = path.integerPartition();
            ArraySelector selector = path.arraySelectorPartition();
            
            if (node.isArray() && integer != null) { // COND: 2
                ArrayNode arrayNode = (ArrayNode) node;
                node = arrayNode.get(integer);
            } else if (selector != null) { // COND: 3

                ArrayNode arrayNode = (ArrayNode) node.get(selector.getArrayPath());

                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objecNode = arrayNode.get(i);

                    String valueToMatch = this.buildValue(selector.getConditionPath(), objecNode, true);

                    if (valueToMatch.equals(selector.getMatcher())) {
                        node = objecNode;
                        break;
                    }
                }
            } else {
                node = node.get(path.getString());
            }
        }

        if (node == null) { // COND: 1
            return null;
        }

        return node.textValue();
    }

    private LinkedList<Path> pathsFromValue(String value) {
        LinkedList<Path> paths = new LinkedList<>();

        StringBuilder pathBuilder = new StringBuilder();
        Boolean lookingForAClose = false;
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);

            if (character == '[') { lookingForAClose = true; }
            if (character == ']') { lookingForAClose = false; }
            if (character == '.' && !lookingForAClose) {
                paths.add(new Path(pathBuilder.toString()));
                pathBuilder.setLength(0);
                continue;
            }

            pathBuilder.append(character);
        }

        paths.add(new Path(pathBuilder.toString()));
        pathBuilder.setLength(0);

        Path first = paths.getFirst();
        String data = first.getString();
        if (data.equals("data")) {
            paths.removeFirst(); 
        }
        return paths;
    }
}
