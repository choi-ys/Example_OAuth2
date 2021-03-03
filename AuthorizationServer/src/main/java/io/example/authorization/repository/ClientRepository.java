package io.example.authorization.repository;

import io.example.authorization.domain.entity.partner.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021/03/02 12:14 오후
 * @Content : ClientEntity객체와 해당 Entity객체에 명시된 DB Table을 연동하는 Repository
 */
public interface ClientRepository extends JpaRepository<ClientEntity, String> {
}