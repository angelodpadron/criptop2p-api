package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.OperationAmountResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.InvalidConsultationDatesException;
import ar.edu.unq.desapp.grupog.criptop2p.model.Role;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.RoleRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.TransactionOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BCRAClient;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers;
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
    private final TransactionOrderRepository transactionOrderRepository;
    private final ModelMapper modelMapper;
    private final BCRAClient bcraClient;
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

    public User getUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.addRole(role);

    }

    public List<TransactionOrderResponseBody> getAllTransactionOrders() {
        List<TransactionOrder> transactionOrders = getUserLoggedIn().getTransactionOrders();

        return transactionOrders
                .stream()
                .map(Mappers::transactionOrderEntityToResponseBody)
                .toList();
    }

    public OperationAmountResponseBody getOperationsAmountBetweenDates(LocalDateTime fromDate, LocalDateTime toDate) throws InvalidConsultationDatesException {

        if (fromDate.isAfter(toDate)) {
            throw new InvalidConsultationDatesException("The start date must be before the end date");
        }

        Long userId = getUserLoggedIn().getId();
        List<TransactionOrder> closedTransactions = transactionOrderRepository.getUserClosedTransactionsBetweenDates(userId, fromDate, toDate);

        Double totalDollarAmount =
                closedTransactions
                        .stream()
                        .mapToDouble(transactionOrder -> transactionOrder.getMarketOrder().getTargetPrice())
                        .sum();

        Double totalPesosAmount = totalDollarAmount * bcraClient.getLastUSDARSQuotation();

        return Mappers.toOperationAmountResponseBody(
                totalDollarAmount,
                totalPesosAmount,
                closedTransactions.stream().map(Mappers::transactionOrderEntityToResponseBody).toList());
    }

    private User userRequestBodyToEntityMapper(UserRequestBody requestBody) {
        return modelMapper.map(requestBody, User.class);
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
