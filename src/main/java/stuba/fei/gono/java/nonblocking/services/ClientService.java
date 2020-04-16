package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Client;

public interface ClientService {

    Mono<Client> getClientById(String id);
    Mono<Boolean> clientExistsById(String id);
}
