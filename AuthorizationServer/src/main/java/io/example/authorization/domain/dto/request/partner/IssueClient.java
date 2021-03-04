package io.example.authorization.domain.dto.request.partner;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author : choi-ys
 * @date : 2021/03/04 11:29 오전
 * @Content : 클라이언트 발급 요청 정보
 */
@Getter @Setter
public class IssueClient {

    private long partnerNo;

    @NotBlank(message = "서비스 이름을 입력하세요")
    @Size(min = 1, max = 25, message = "서비스 이름은 1 ~ 25자 이내로 입력 가능합니다.")
    private String resourceIds;
}