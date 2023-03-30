package com.sdui.builder.app.models.layout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    @JsonProperty("content_id")
    private String id;
    @JsonProperty("content_order")
    private Integer order;
    @JsonProperty("value")
    private String value;
}
