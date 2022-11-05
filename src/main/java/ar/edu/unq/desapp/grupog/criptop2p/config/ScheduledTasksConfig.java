package ar.edu.unq.desapp.grupog.criptop2p.config;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.CryptoQuotationRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BCRAClient;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BinanceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@ConditionalOnProperty(name = "schedulers.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {

    private final BinanceClient binanceClient;
    private final BCRAClient bcraClient;
    private final CryptoQuotationRepository cryptoQuotationRepository;

    @Scheduled(fixedRateString = "${quotations.interval}")
    public void retrieveAndSaveAllQuotations() {

        log.info("Obtaining asset prices...");

        List<CryptoQuotationResponseBody> rawQuotations = binanceClient.getAllQuotations();
        Double usdExchange = bcraClient.getLastUSDARSQuotation();
        List<CryptoQuotation> entityQuotations = rawQuotations.stream().map(raw -> new CryptoQuotation(raw.getSymbol(), raw.getPrice(), usdExchange)).toList();

        cryptoQuotationRepository.saveAll(entityQuotations);

        log.info("All asset prices has been retrieved.");

    }


}
