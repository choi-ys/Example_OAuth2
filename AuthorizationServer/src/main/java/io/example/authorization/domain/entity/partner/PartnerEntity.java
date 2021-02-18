package io.example.authorization.domain.entity.partner;

import io.example.authorization.domain.entity.common.MetaEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "tb_partner"
        , uniqueConstraints = {
                @UniqueConstraint(name = "PARTNER_ID_UNIQUE", columnNames = "partner_id")
            }
        )
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class PartnerEntity extends MetaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_no")
    private Long partnerNo; // Primary Key : Auto Increment

    @Column(name = "partner_id", nullable = false, length = 50)
    private String partnerId; // 제휴사 ID : Unique key

    @Column(name = "partner_password", nullable = false, length = 50)
    private String partnerPassword; // 제휴사 비밀번호

    @Column(name = "partner_email", nullable = false, length = 50)
    private String partnerEmail; // 제휴사 email

    @Column(name = "partner_company_name",nullable = false, length = 50)
    private String partnerCompanyName; // 제휴사 명

    @Column(name = "client_id", nullable = true, length = 100)
    private String clientId; // Token 발급을 위한 Client ID

    @Column(name = "client_secret", nullable = true, length = 100)
    private String clientSecret; // Token 발급을 위한 Client Secret

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tb_partner_role"
            , joinColumns = @JoinColumn(
                    name = "partner_no"
                    , foreignKey = @ForeignKey(name = "PARTNER_NO_FOREIGN_KEY")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "partner_role", nullable = false, length = 10)
    private Set<PartnerRole> partnerRoles; // 파트너에게 부여된 권한

    @Enumerated(EnumType.STRING)
    private PartnerStatus partnerStatus; // 파트너 상태
}