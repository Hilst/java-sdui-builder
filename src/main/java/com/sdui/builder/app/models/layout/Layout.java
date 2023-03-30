package com.sdui.builder.app.models.layout;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Layout {
    @JsonProperty("layout_code")
    private String code;
    @JsonProperty("layout_pages")
    private List<Page> pages;
}
