package com.sdui.builder.app.models.layout;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
    @JsonProperty("page_id")
    private String id;
    @JsonProperty("page_order")
    private Integer order;
    @JsonProperty("page_sections")
    private List<Section> sections;
}
