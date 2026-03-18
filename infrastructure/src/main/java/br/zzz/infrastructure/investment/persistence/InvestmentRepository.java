package br.zzz.infrastructure.investment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentJpaEntity, String> {

    List<InvestmentJpaEntity> findAllByWalletId(String walletId);

}
