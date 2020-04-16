package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.services.ClientService;
import stuba.fei.gono.java.pojo.Client;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

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
