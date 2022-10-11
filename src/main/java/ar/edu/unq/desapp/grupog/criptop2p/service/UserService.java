package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.Role;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.MarketOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.RoleRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MarketOrderRepository marketOrderRepository;
    private final CryptoQuotationService cryptoQuotationService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    public User saveUser(UserRequestBody userRequestBody) throws EmailAlreadyTakenException {

        checkIfEmailIsAlreadyTaken(userRequestBody.getEmail());

        User user = userRequestBodyToEntityMapper(userRequestBody);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("Saving new user {} to the database", user.getEmail());

        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void addMarketOrderToUser(MarketOrderRequestBody marketOrderRequestBody) throws MarketOrderException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Double marketPrice = cryptoQuotationService.getQuotation(marketOrderRequestBody.getCryptocurrency()).getPriceInUSD();

        MarketOrder marketOrder = marketOrderRequestBodyToEntityMapper(marketOrderRequestBody, user, marketPrice);

        marketOrderRepository.save(marketOrder);
        user.addMarketOrder(marketOrder);

    }

    public List<MarketOrderResponseBody> getMarketOrders() {
        List<MarketOrder> marketOrders = marketOrderRepository.findAll();
        List<MarketOrderResponseBody> marketOrderResponseBodies = new ArrayList<>();

        marketOrders.forEach(marketOrder -> marketOrderResponseBodies.add(marketOrderEntityToResponseBody(marketOrder)));

        return marketOrderResponseBodies;

    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.addRole(role);

    }

    private User userRequestBodyToEntityMapper(UserRequestBody requestBody) {
        return modelMapper.map(requestBody, User.class);
    }

    private MarketOrderResponseBody marketOrderEntityToResponseBody(MarketOrder marketOrder) {
        return new MarketOrderResponseBody(
                marketOrder.getId(),
                marketOrder.getCreator().getEmail(),
                marketOrder.getCreationDate(),
                marketOrder.getCryptocurrency(),
                marketOrder.getNominalAmount(),
                marketOrder.getMarketPrice(),
                marketOrder.getTargetPrice(),
                marketOrder.getOperation(),
                marketOrder.getAvailable());
    }

    private MarketOrder marketOrderRequestBodyToEntityMapper(MarketOrderRequestBody marketOrderRequestBody,
                                                             User user,
                                                             Double marketPrice) throws MarketOrderException {
        return new MarketOrder(
                null,
                LocalDateTime.now(),
                marketOrderRequestBody.getCryptocurrency(),
                marketOrderRequestBody.getNominalAmount(),
                marketPrice,
                marketOrderRequestBody.getTargetPrice(),
                user,
                marketOrderRequestBody.getOperation());
    }

    private void checkIfEmailIsAlreadyTaken(String email) throws EmailAlreadyTakenException {
        if (emailAlreadyTaken(email)) {
            throw new EmailAlreadyTakenException("There is an account with that email address:" + email);
        }
    }

    private boolean emailAlreadyTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }

    // spring security

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User with email {} not found", email);
            throw new UsernameNotFoundException("User not found");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));


        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
