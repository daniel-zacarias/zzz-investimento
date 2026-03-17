package br.zzz.infrastructure.wallet.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletJpaEntity, String> {

    @EntityGraph(attributePaths = "investments")
    Optional<WalletJpaEntity> findByUserId(String userId);

}
