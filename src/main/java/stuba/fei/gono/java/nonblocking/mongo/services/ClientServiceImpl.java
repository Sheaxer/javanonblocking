package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.services.ClientService;
import stuba.fei.gono.java.pojo.Client;

/***
 * <div class="en">Implementation of service that manages
 * marshalling and de-marshalling Client objects using CRUD operations and ClientRepository
 * instance.</div>
 * <div class="sk">Implementácia služby ktorá spravuje marshalling a de-marshalling objektov triedy Client
 * pomocou CRUD operácií a inštanciu rozhrania ClientRepository.</div>
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 * @see ClientRepository
 */
@Service
public class ClientServiceImpl implements ClientService {
    /***
     * <div class="en">Repository providing CRUD operations on Client entities.</div>
     * <div class="sk">Repozitár poskytujúce CRUD operácíe nad entitami triedy Client.</div>
     * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
     */
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public Mono<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }


    @Override
    public Mono<Boolean> clientExistsById(String id) {
        return clientRepository.existsById(id);
    }
}
