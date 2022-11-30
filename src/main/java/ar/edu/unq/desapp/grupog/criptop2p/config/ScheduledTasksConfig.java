package ar.edu.unq.desapp.grupog.criptop2p.config;

import ar.edu.unq.desapp.grupog.criptop2p.service.CryptoQuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@EnableScheduling
@ConditionalOnProperty(name = "schedulers.enabled", matchIfMissing = true)
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {

    private final CryptoQuotationService cryptoQuotationService;
    private final RedisCacheManager redisCacheManager;

    @Scheduled(fixedRateString = "${quotations.interval}")
    public void updateLocalQuotations() {
        log.info("Updating local quotations data...");
        cryptoQuotationService.updateLocalQuotations();
        clearCache();
        log.info("Local quotations data has been updated");
    }

    private void clearCache() {
        log.info("Cleaning quotations data cache...");
        redisCacheManager
                .getCacheNames()
                .parallelStream()
                .forEach(cacheName -> redisCacheManager.getCache(cacheName).clear());
    }


}
