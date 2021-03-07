package com.example.resource.controller;

import com.example.resource.constants.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/resource", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ResourceController {

    @GetMapping("/any")
    public ResponseEntity resourceToAny(){
        String content = "resource to any";
        return ResponseEntity.ok(content);
    }

    @GetMapping("/role")
    public ResponseEntity resourceToHasRole(){
        String content = "resource to has role";
        return ResponseEntity.ok(content);
    }
}