package br.zzz.investimento.investmentservice.wallet;

import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class HttpWalletGateway implements WalletGateway {

    private final RestClient restClient;

    public HttpWalletGateway(@Value("${wallet-service.base-url}") final String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Wallet create(Wallet wallet) {
        throw new UnsupportedOperationException("create not supported by HttpWalletGateway");
    }

    @Override
    public boolean existsById(final WalletID id) {
        try {
            restClient.get()
                    .uri("/api/internal/wallets/{id}/exists", id.getValue())
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw ex;
        }
    }

    @Override
    public Pagination<Wallet> findAllByUserId(WalletSearchQuery query) {
        throw new UnsupportedOperationException("findAllByUserId not supported by HttpWalletGateway");
    }

    @Override
    public List<Wallet> findAllByUserId(final UserID userId) {
        throw new UnsupportedOperationException("findAllByUserId not supported by HttpWalletGateway");
    }
}
