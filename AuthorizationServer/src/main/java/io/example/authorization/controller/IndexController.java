package io.example.authorization.controller;

import io.example.authorization.constants.MediaTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author : choi-ys
 * @date : 2021/03/08 2:39 오후
 * @Content :
 */
@RestController
@RequestMapping(value = "/api/index", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class IndexController {

    @GetMapping
    public RepresentationModel representationModel(){
        RepresentationModel indexRepresentationModel = new RepresentationModel();
        indexRepresentationModel.add(linkTo(this.getClass()).withRel(INDEX));
        log.info("index api");
        return indexRepresentationModel;
    }
}
