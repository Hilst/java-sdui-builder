package com.sdui.builder.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sdui.builder.app.models.layout.Content;
import com.sdui.builder.app.models.layout.Layout;
import com.sdui.builder.app.models.layout.Page;
import com.sdui.builder.app.models.layout.Section;

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

            if (!page.getSections().isEmpty()) {
                newPages.add(page);
            }
        }

        return newPages;
    }

    private List<Section> buildSections(List<Section> sections, JsonNode data) {
        List<Section> newSections = new ArrayList<>();

        for (Section section : sections) {
            section.setContents(buildContents(section.getContents(), data));

            if (!section.getContents().isEmpty()) {
                newSections.add(section);
            }
        }

        return newSections;
    }

    private List<Content> buildContents(List<Content> contents, JsonNode data) {
        List<Content> newContents = new ArrayList<>();

        for (Content content : contents) {

            String value = null;
            if (content.getValue().contains("data/")) {
                value = buildValue(content.getValue().substring(4), data, false);
            } else if (!content.getValue().isBlank()) {
                value = content.getValue();
            }

            if (value != null) {
                content.setValue(value);
                newContents.add(content);
            }
        }

        return newContents;
    }

    private String buildValue(String value, JsonNode data, Boolean fromNode) {
        JsonNode node = data.at(value);
        return treatmentNodeOutput(node);
    }

    private String treatmentNodeOutput(JsonNode node) {
        if (node.isNull() || !node.isValueNode()) {
            return null;
        }
        switch (node.getNodeType()) {
            case STRING:
                return node.textValue();
            case NUMBER:
                return node.numberValue().toString();
            default:
                return null;
        }
    }
}
