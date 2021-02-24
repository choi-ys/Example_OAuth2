package io.example.authorization.controller;

import io.example.authorization.domain.dto.request.CreatePartner;
import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.example.authorization.constants.MediaTypes.HAL_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/api/partner", produces = HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity createPartner(CreatePartner createPartner){
        String msg = "요청이 성공적으로 처리 되었습니다.";
        if(createPartner != null){
            return ResponseEntity.ok(msg);
        }else{
            msg = "잘못된 요청입니다. 다시 시도해 주세요";
            return ResponseEntity.badRequest().body(msg);
        }
    }
}