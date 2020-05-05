package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Client;
/***
 * Interface for marshalling and de-marshalling Client entities.
 */
public interface ClientService {
    /***
     * <div class="en">Finds the entity with the given id.</div>
     * <div class="sk">Nájde entitu so zadaným id.</div>
     * @param id <div class="en">id of the entity, must not be null.</div>
     *           <div class="sk">id entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity was found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak entitu nie je možné nájsť.</div>
     */
    Mono<Client> getClientById(String id);

    /***
     * <div class="en">Checks if the entity with the given id was found.</div>
     * <div class="sk">Skontroluje, či entita so zadaným id existuje.</div>
     * @param id <div class="en">id of the entity, must not be null.</div>
     *           <div class="sk">id entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting true if the entity was found,
     * false otherwise.</div>
     * <div class="sk">Mono emitujúce true ak entita existuje, false inak.</div>
     */
    Mono<Boolean> clientExistsById(String id);
}
