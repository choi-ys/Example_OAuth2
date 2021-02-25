package io.example.authorization.controller.common;

import io.example.authorization.controller.PartnerController;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.dto.response.resource.ErrorsEntityModel;
import io.example.authorization.domain.dto.response.resource.ProcessingResultEntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author : choi-ys
 * @date : 2021-02-25 오후 11:57
 * @Content : 각 Controller에서 공통으로 사용하는 응답처리
 */
public class CommonResponseEntity {

    /**
     * 400 BadReqeust 응답 처리
     */
    public static ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorsEntityModel(errors));
    }

    /**
     * POST 요청의 created 응답 처리
     */
    public static ResponseEntity<ProcessingResultEntityModel> createResponse(ProcessingResult processingResult) {
        URI location = linkTo(PartnerController.class).withSelfRel().toUri();
        return ResponseEntity.created(location).body(new ProcessingResultEntityModel(processingResult));
    }

    /**
     * Service 처리 결과가 성공이 아닌경우 오류 응답 처리
     */
    public static ResponseEntity errorResponse(ProcessingResult processingResult) {
        int errorCode = processingResult.getError().getCode();
        switch (errorCode){
            case 412:
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(processingResult.getError());
            case 500:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(processingResult.getError());
            default:
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(processingResult.getError());
        }
    }
}
