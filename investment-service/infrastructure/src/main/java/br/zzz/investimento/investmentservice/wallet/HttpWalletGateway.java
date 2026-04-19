package br.zzz.investimento.investmentservice.wallet;

import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.Objects;

@Component
public class HttpWalletGateway implements WalletGateway {

    private final WalletServiceClient walletServiceClient;

    public HttpWalletGateway(final WalletServiceClient walletServiceClient) {
        this.walletServiceClient = Objects.requireNonNull(walletServiceClient);
    }

    @Override
    public boolean existsById(final WalletID id) {
        try {
            return walletServiceClient.existsById(id.getValue()).getStatusCode().is2xxSuccessful();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw ex;
        }
    }
}
