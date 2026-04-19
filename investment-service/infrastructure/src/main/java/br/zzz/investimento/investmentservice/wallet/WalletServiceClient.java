package br.zzz.investimento.investmentservice.wallet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wallet-service", url = "${wallet-service.base-url}")
public interface WalletServiceClient {

    @GetMapping("/api/internal/wallets/{id}/exists")
    ResponseEntity<?> existsById(@PathVariable("id") String id);
}
