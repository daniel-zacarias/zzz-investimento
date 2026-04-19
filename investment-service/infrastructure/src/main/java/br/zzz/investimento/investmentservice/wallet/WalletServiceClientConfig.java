package br.zzz.investimento.investmentservice.wallet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WalletServiceClientConfig {

    @Bean
    public WalletServiceClient walletServiceClient(
            @Value("${wallet-service.base-url}") final String baseUrl
    ) {
        final var restClient = RestClient.builder().baseUrl(baseUrl).build();
        final var adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory
                .builderFor(adapter)
                .build()
                .createClient(WalletServiceClient.class);
    }
}
