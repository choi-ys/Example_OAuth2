package io.example.authorization.domain.dto.response.resource;

/**
 * @author : choi-ys
 * @date : 2021/02/25 5:18 오후
 * @Content :
 */
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

public class ErrorsEntityModel extends EntityModel<Errors> {

    public ErrorsEntityModel(Errors content, Link... links) {
        super(content, links);
    }
}