package io.example.authorization.domain.entity.partner;

/**
 * 제휴사 계정 상태
 */
public enum PartnerStatus {
    API_NOT_AVAILABLE,      // 제휴 진행 전 상태 [기본값] : Client ID / Secret 발급전 API 사용 권한 없는 NEWBIE 권한
    DRAFT,                  // 제휴 진행 전 상태 : 기본적인 API 통신만이 허용된 STARTER 권한
    ALLIANCE,               // 제휴 진행 상태 : 과금방식에 따라 API 허용량이 다른 STANDARD, PREMINUM의 권한
    EXPIRATION,             // 제휴 만료 상태 : FORBIDDEN 권한
    STOP_USING_BY_ADMIN,    // 사내 관리자에 의한 제휴 정지 -> FORBIDDEN 권한
    STOP_USING_BY_PARTNER   // 파트너사 요청에 의한 제휴 정지 -> STARTER 권한
}