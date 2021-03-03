package io.example.authorization.controller;

import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.example.authorization.constants.MediaTypes.HAL_JSON_UTF8_VALUE;
import static io.example.authorization.controller.common.CommonResponseEntity.*;

/**
 * @author : choi-ys
 * @date : 2021-02-25 오후 11:58
 * @Content : Partner 관련 요청 처리 Controller
 */
@RestController
@RequestMapping(value = "/api/partner", produces = HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class PartnerController {

    private final PartnerService partnerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createPartner(@RequestBody @Valid CreatePartner createPartner, Errors errors){
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        ProcessingResult processingResult = this.partnerService.savePartner(createPartner);

        if(processingResult.isSuccess()){
            return createResponse(processingResult);
        }else{
            return errorResponse(processingResult);
        }
    }
}