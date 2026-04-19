package br.zzz.infrastructure.investment.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.client.support.RestClientAdapter;

@Configuration
public class InvestmentServiceClientConfig {

    @Bean
    public InvestmentServiceClient investmentServiceClient(
            final RestClient.Builder builder,
            @Value("${investment-service.base-url:http://localhost:8081}") final String baseUrl
    ) {
        final var restClient = builder.baseUrl(baseUrl).build();
        final var adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory
                .builderFor(adapter)
                .build()
                .createClient(InvestmentServiceClient.class);
    }
}
