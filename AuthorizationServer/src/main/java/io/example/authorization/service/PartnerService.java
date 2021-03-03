package io.example.authorization.service;

import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.dto.response.common.Error;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.entity.partner.PartnerRole;
import io.example.authorization.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static io.example.authorization.service.common.CommonResult.serverError;

/**
 * @date : 2021/02/19 12:08 오후
 * @author : choi-ys
 * @Content : 사용자 계정 처리 Service
**/
@Service
@RequiredArgsConstructor
public class PartnerService implements UserDetailsService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * 인증 토큰 발급 대상자인 사용자 계정 생성 로직
     *  - partnerId 항목 중복 검사
     *  - 비밀번호 항목 암호화 : passwordEncoder를 이용한 Bcript 암호화
     *  - 입력값에 의해 결정되는 값 설정 : 사용자의 초기 권한 및 상태
     *  - DB 저장 및 처리 결과 반환 처리
     *  - 로직 처리 중 발생하는 예외 반환 처리
     * @param createPartner
     * @return
     */
    public ProcessingResult savePartner(CreatePartner createPartner){
        if(this.isDuplicatedId(createPartner.getPartnerId())){
            return new ProcessingResult(Error.builder()
                    .code(-1)
                    .message("이미 존재하는 ID 입니다.")
                    .build()
            );
        }

        PartnerEntity partnerEntity = this.modelMapper.map(createPartner, PartnerEntity.class);
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

    /**
     * Application에서 정의한 Account domain을 Spring security에서 정의한 UsertDetail Interface로 변환
     * @param partnerId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String partnerId) throws UsernameNotFoundException {
        PartnerEntity partnerAccountEntity = partnerRepository.findByPartnerId(partnerId)
                /**
                 * 요청 파라미터에 해당하는 Account 객체 조회 실패 시 오류 반환 처리
                 * - userName(account.email)에 해당 하는 Account 객체 조회 실패 시 Null을 반환 하므로
                 * - Srping security의 UsernameNotFoundException객체를 통해 정의된 오류를 반환한다.
                 */
                .orElseThrow(() -> new UsernameNotFoundException(partnerId));

        /**
         * Srping security의 User객체를 이용하여 MemberEntity객체를 UserDetails 객체로 변환
         *  - UserDetails interface로 객체 변환 처리를 구현할 경우 모든 메소드를 구현 해야하므로,
         *  - UserDetails의 User객체를 이용하여 MemberEntity 체를 Spring Security의 UserDetails 객체로 변환한다.
         */
        return new User(partnerAccountEntity.getPartnerId(),
                partnerAccountEntity.getPartnerPassword(),
                this.roleToAuthorities(partnerAccountEntity.getPartnerRoles())
        );
    }

    /**
     * Set으로 정의된 Role을 Collection Type으로 변환
     * @param roles
     * @return
     */
    private Collection<? extends GrantedAuthority> roleToAuthorities(Set<PartnerRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
