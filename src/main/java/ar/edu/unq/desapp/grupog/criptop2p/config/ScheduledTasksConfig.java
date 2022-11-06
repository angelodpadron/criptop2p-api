package ar.edu.unq.desapp.grupog.criptop2p.config;

import ar.edu.unq.desapp.grupog.criptop2p.service.CryptoQuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @Scheduled(fixedRateString = "${quotations.interval}")
    public void updateLocalQuotations() {
        cryptoQuotationService.updateLocalQuotations();
        log.info("Local quotations data has been updated");
    }


}
