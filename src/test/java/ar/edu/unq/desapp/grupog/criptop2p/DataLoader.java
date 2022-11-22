package ar.edu.unq.desapp.grupog.criptop2p;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.model.QuotationData;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.CryptoQuotationRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.QuotationDataRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoQuotationRepository cryptoQuotationRepository;
    @Autowired
    private QuotationDataRepository quotationDataRepository;
    @Autowired
    private ModelMapper modelMapper;

    // User data

    public void createUser(User user) throws EmailAlreadyTakenException {
        UserRequestBody asRequestBody = modelMapper.map(user, UserRequestBody.class);
        userService.saveUser(asRequestBody);
    }

    public void deleteAllUsersData() {
        userRepository.deleteAll();
    }

    // Quotations data

    public void createQuotationData(String symbol, Double priceInUsd, Double priceInArs) {

        CryptoQuotation cryptoQuotation = new CryptoQuotation(symbol, new ArrayList<>());
        cryptoQuotationRepository.save(cryptoQuotation);
        QuotationData quotationData = new QuotationData(cryptoQuotation, priceInUsd, priceInArs);
        quotationDataRepository.save(quotationData);
    }

    public void deleteAllQuotationsData() {
        cryptoQuotationRepository.deleteAll();
        quotationDataRepository.deleteAll();
    }

}
