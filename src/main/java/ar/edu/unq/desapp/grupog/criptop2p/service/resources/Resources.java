package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import java.util.List;

public final class Resources {

    private Resources() {
    }

    public static final List<String> CRYPTO_SYMBOLS = List.of(
            "ALICEUSDT",
            "MATICUSDT",
            "AXSUSDT",
            "AAVEUSDT",
            "ATOMUSDT",
            "NEOUSDT",
            "DOTUSDT",
            "ETHUSDT",
            "CAKEUSDT",
            "BTCUSDT",
            "BNBUSDT",
            "ADAUSDT",
            "TRXUSDT",
            "AUDIOUSDT");
    public static final String BCRA_USD_QUOTATION_URL = "https://api.estadisticasbcra.com/usd_of";
    public static final String BINANCE_ALL_QUOTATIONS_URL = "https://api1.binance.com/api/v3/ticker/price?symbols=";
    public static final String BINANCE_QUOTATION_URL = "https://api1.binance.com/api/v3/ticker/price?symbol=";
}
