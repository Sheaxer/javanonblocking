package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.AccountRepository;
import stuba.fei.gono.java.nonblocking.services.AccountService;
import stuba.fei.gono.java.pojo.Account;

/***
 * MongoDB implementation of service that manages marshalling and de-marshalling Account objects.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /***
     * Retrieves the Account  identified by IBAN
     * @param iban IBAN of desired Account, must not be null
     * @return Mono emitting the Account identified by given IBAN or Mono.empty() if none found.
     */
    @Override
    public Mono<Account> getAccountByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    /***
     *
     * @param number Local Account Number of desired Account
     * @return Mono of Account identified by the given Local account number or Mono.empty() if none found.
     */
    @Override
    public Mono<Account> getAccountByLocalAccountNumber(String number) {
        return accountRepository.findAccountByLocalAccountNumber(number);
    }
}
