package io.example.authorization.domain.dto.response.resource;

import io.example.authorization.domain.dto.response.common.ProcessingResult;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

/**
 * @author : choi-ys
 * @date : 2021-02-25 오후 11:58
 * @Content : 로직 처리 결과 및 Link정보를 포함한 Resource를 반환하기 위한 EntityModel
 */
public class ProcessingResultEntityModel extends EntityModel<ProcessingResult> {

    public ProcessingResultEntityModel(ProcessingResult processingResult, Link... links) {
        super(processingResult, links);
    }
}