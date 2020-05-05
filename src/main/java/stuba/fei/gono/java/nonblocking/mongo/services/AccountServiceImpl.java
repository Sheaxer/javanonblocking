package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.AccountRepository;
import stuba.fei.gono.java.nonblocking.services.AccountService;
import stuba.fei.gono.java.pojo.Account;

/***
 * <div class="en">Implementation of service that manages marshalling and de-marshalling Account objects using CRUD
 * operations and AccountRepository instance.</div>
 * <div class="sk">Implementácia služby ktorá spravuje marshalling a de-marhalling objektov triedy Account
 * pomocou CRUD operácii a inštanciu rozhrania AccountRepository</div>
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 * @see AccountRepository
 */
@Service
public class AccountServiceImpl implements AccountService {
    /***
     * <div class="en">Repository providing CRUD operations on Account entities.</div>
     * <div class="sk">Repozitár poskytujúce CRUD operácie nad entitami triedy Account.</div>
     * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
     */
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
