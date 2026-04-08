package br.zzz.infrastructure.wallet.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletJpaEntity, String> {

    @EntityGraph(attributePaths = "investments")
    List<WalletJpaEntity> findAllByUserId(String userId);

}
