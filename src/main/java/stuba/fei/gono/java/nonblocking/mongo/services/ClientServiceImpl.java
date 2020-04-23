package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.services.ClientService;
import stuba.fei.gono.java.pojo.Client;

/***
 *MongoDB implementation of service that handles marshalling and de-marshalling Client objects.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /***
     * Retrieves Client by its id
     * @param id must not be null
     * @return Mono emitting the Client with the given id or Mono.empty() if none found.
     */
    @Override
    public Mono<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    /***
     * Checks if Client is stored in the database
     * @param id id of client
     * @return Mono emitting true if Client with the given id exists, false otherwise.
     */
    @Override
    public Mono<Boolean> clientExistsById(String id) {
        return clientRepository.existsById(id);
    }
}
