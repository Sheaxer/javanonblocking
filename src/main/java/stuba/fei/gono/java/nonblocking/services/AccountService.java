package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

public interface AccountService {
    Mono<Account> getAccountByIban(String iban);
    Mono<Account> getAccountByLocalAccountNumber(String number);
}
