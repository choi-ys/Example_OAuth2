package io.example.authorization.controller;

import io.example.authorization.domain.dto.request.CreatePartner;
import io.example.authorization.domain.dto.response.resource.ErrorsEntityModel;
import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.example.authorization.constants.MediaTypes.HAL_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/api/partner", produces = HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity createPartner(@RequestBody @Valid CreatePartner createPartner, Errors errors){
        String msg = "요청이 성공적으로 처리 되었습니다.";
        if(errors.hasErrors()){
            msg = "잘못된 요청입니다. 다시 시도해 주세요";
            return this.badRequest(errors);
        }else{
            return ResponseEntity.ok(msg);
        }
    }

    public ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorsEntityModel(errors));
    }
}