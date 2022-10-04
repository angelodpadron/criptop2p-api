package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import java.util.List;

public class Resources {

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
    public static final String BCRA_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTYxNzIwNTYsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJwYWRyb244OTFAZ21haWwuY29tIn0.mr0WtapCWVvD3siAXLqEkFcfN2oH1bSTDcURosX7VN5z3xCuiiWf9avRA5XwAI3fB27LlN7gZxMeWho1wDGHSA";
    public static final String BINANCE_ALL_QUOTATIONS_URL = "https://api1.binance.com/api/v3/ticker/price?symbols=";
}
