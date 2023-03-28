package com.sdui.builder.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.sdui.builder.app.model.data.Data;
import com.sdui.builder.app.model.layout.Layout;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Input {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("layout")
    private Layout layout;
}
