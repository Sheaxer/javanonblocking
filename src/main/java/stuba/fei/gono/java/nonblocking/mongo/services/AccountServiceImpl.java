package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.AccountRepository;
import stuba.fei.gono.java.nonblocking.services.AccountService;
import stuba.fei.gono.java.pojo.Account;
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Account> getAccountByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    @Override
    public Mono<Account> getAccountByLocalAccountNumber(String number) {
        return accountRepository.findAccountByLocalAccountNumber(number);
    }
}
