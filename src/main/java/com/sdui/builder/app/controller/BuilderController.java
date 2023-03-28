package com.sdui.builder.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdui.builder.app.model.dto.Input;
import com.sdui.builder.app.model.layout.Layout;
import com.sdui.builder.app.service.Builder;

@RestController
public class BuilderController {

    @Autowired
    private Builder builder;

    @GetMapping(value = "/")
    public String index() {
        return "SDUI Builder is here";
    }

    @PostMapping(value = "build")
    public Layout build(@RequestBody Input input) {
        return builder.buildLayoutWithData(input.getLayout(), input.getData().getData());
    }
    
}
    