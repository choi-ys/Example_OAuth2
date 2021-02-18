package io.example.authorization.domain.entity.partner;

import javax.persistence.Table;

/**
 * 제휴사 계정 권한
 */
public enum PartnerRole {
    FORBIDDEN,          // API 사용 권한 없음
    STARTER,            // 일부 API만 통신 가능 및 허용량 제한
    ALLIANCE,           // 모든 API 통신 가능 및 허용량 없음
}