package io.example.authorization.repository;

import io.example.authorization.domain.entity.partner.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {

    long countByPartnerId(String id);
    Optional<PartnerEntity> findByPartnerId(String partnerId);
    Optional<PartnerEntity> findByPartnerNo(long partnerNo);
}