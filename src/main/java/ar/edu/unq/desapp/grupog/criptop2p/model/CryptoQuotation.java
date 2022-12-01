package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoQuotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cryptoQuotation")
    private List<QuotationData> quotationData;

    public CryptoQuotation(String symbol, List<QuotationData> quotationData) {
        this.symbol = symbol;
        this.quotationData = quotationData;
    }

    public void addQuotationData(QuotationData quotationData) {
        this.quotationData.add(quotationData);
    }

    public QuotationData getLastQuotation() {
        return !quotationData.isEmpty() ? quotationData.get(quotationData.size() - 1) : null;
    }
}
