package com.sdui.builder.app.model.layout;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    @JsonProperty("section_id")
    private String id;
    @JsonProperty("section_order")
    private Integer order;
    @JsonProperty("section_contents")
    private List<Content> contents;    
}
