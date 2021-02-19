package io.example.authorization.service;

import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.dto.response.common.Error;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static io.example.authorization.service.common.CommonResult.serverError;

/**
 * @date : 2021/02/19 12:08 오후
 * @author : choi-ys
 * @Content : 사용자 계정 처리 Service
**/
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증 토큰 발급 대상자인 사용자 계정 생성 로직
     *  - partnerId 항목 중복 검사
     *  - 비밀번호 항목 암호화 : passwordEncoder를 이용한 Bcript 암호화
     *  - 입력값에 의해 결정되는 값 설정 : 사용자의 초기 권한 및 상태
     *  - DB 저장 및 처리 결과 반환 처리
     *  - 로직 처리 중 발생하는 예외 반환 처리
     * @param partnerEntity
     * @return
     */
    public ProcessingResult savePartner(PartnerEntity partnerEntity){
        if(this.isDuplicatedId(partnerEntity.getPartnerId())){
            return new ProcessingResult(Error.builder()
                    .code(-1)
                    .message("이미 존재하는 ID 입니다.")
                    .build()
            );
        }

        partnerEntity.setPartnerPassword(this.passwordEncoder.encode(partnerEntity.getPartnerPassword()));
        partnerEntity.signUp();

        try {
            return new ProcessingResult(this.partnerRepository.save(partnerEntity));
        } catch (Exception e) {
            return serverError(e);
        }
    }

    private boolean isDuplicatedId(String partnerId){
        long count = partnerRepository.countByPartnerId(partnerId);
        return (count == 0) ? false : true;
    }
}
