package com.sdui.builder.app.utils;

import lombok.Getter;

@Getter
public class ArraySelector {
    private String arrayPath;
    private String conditionPath;
    private String matcher;

    public ArraySelector(String path, String condition, String matcher) {
        super();
        this.arrayPath = path.trim();
        this.conditionPath = condition.trim();
        this.matcher = matcher.trim();
    }
}
