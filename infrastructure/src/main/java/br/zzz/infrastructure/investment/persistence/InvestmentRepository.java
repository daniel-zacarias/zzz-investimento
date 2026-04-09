package br.zzz.infrastructure.investment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentJpaEntity, String> {

    List<InvestmentJpaEntity> findAllByWalletId(String walletId);

    @Query("select i.walletId as walletId, coalesce(sum(i.result), 0) as totalResult " +
            "from Investment i " +
            "where i.walletId in :walletIds " +
            "group by i.walletId")
    List<WalletTotalResultProjection> sumResultByWalletIds(@Param("walletIds") List<String> walletIds);

    interface WalletTotalResultProjection {
        String getWalletId();

        Double getTotalResult();
    }
}
