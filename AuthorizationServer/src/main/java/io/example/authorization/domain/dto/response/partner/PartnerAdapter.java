package io.example.authorization.domain.dto.response.partner;

import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.entity.partner.PartnerRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/03/05 9:47 오전
 * @Content : 서비스에서 사용자를 표현하는 PartnerEntity 객체를 SpringSecurity의 UserDetails 객체로 변환
 */
public class PartnerAdapter extends User {

    public PartnerAdapter(PartnerEntity partnerEntity) {
        super(partnerEntity.getPartnerId(), partnerEntity.getPartnerPassword(), roleToAuthorities(partnerEntity.getPartnerRoles()));
    }

    /**
     * Set으로 정의된 Role을 Collection Type으로 변환
     * @param roles Collection으로 변환할 권한 Set
     * @return
     */
    private static Collection<? extends GrantedAuthority> roleToAuthorities(Set<PartnerRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}