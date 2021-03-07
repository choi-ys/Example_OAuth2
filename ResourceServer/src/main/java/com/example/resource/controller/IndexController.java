package com.example.resource.controller;

import com.example.resource.constants.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/index", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class IndexController {

    @GetMapping
    public RepresentationModel index(){
        RepresentationModel indexRepresentationModel = new RepresentationModel();
        indexRepresentationModel.add(linkTo(this.getClass()).withRel(INDEX));
        return indexRepresentationModel;
    }
}
