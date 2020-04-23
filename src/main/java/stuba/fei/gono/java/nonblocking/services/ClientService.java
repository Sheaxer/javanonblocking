package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Client;
/***
 * Interface for marshalling and de-marshalling Client entities.
 */
public interface ClientService {
    /***
     * Retrieve entity by the given id.
     * @param id must not be null
     * @return Mono emitting the entity or Mono.empty() if no entity was found.
     */
    Mono<Client> getClientById(String id);

    /***
     * Checks if the entity with the given id was found.
     * @param id must not be null
     * @return Mono emitting true if the entity was found, false otherwise.
     */
    Mono<Boolean> clientExistsById(String id);
}
