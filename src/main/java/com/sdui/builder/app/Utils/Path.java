package com.sdui.builder.app.Utils;

import lombok.Getter;

@Getter
public class Path {
    private String string;

    public Path(String string) {
        super();
        this.string = string.trim();
    }

    public Integer integerPartition() {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return null;
        }
    }

    public ArraySelector arraySelectorPartition() {
        Integer indexOpen = string.indexOf("[");
        Integer indexEquals = string.indexOf("==");
        Integer indexClose = string.indexOf("]");
        
        if (indexOpen == indexEquals || indexOpen == indexClose || indexClose == indexEquals) { return null; }

        try {
            String path = string.substring(0, indexOpen);
            String conditionPath = string.substring(indexOpen + 1, indexEquals);
            String matcher = string.substring(indexEquals + 2, indexClose);
            return new ArraySelector(path, conditionPath, matcher);
        } catch (Exception e) {
            return null;
        }
    }
}
