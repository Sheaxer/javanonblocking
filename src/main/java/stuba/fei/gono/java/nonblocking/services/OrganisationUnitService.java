package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.OrganisationUnit;
/***
 * <div class="en">Interface for marshalling and de-marshalling OrganisationUnit entities.</div>
 * <div class="sk">Rozhranie na marshalling a de-marshalling entít triedy OrganisationUnit.</div>
 */

public interface OrganisationUnitService {
    /***
     * <div class="en">Finds the entity with the given id.</div>
     * <div class="sk">Nájde entitu so zadaným id.</div>
     * @param id <div class="en">id of the entity, must not be null.</div>
     *           <div class="sk">id entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if the entity was not found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.emtpy() ak entita neexistuje.</div>
     */
    Mono<OrganisationUnit> getOrganisationUnitById(String id);

    /***
     * <div class="en">Checks if the entity with the given id exists.</div>
     * <div class="sk">Skontroluje, či entita so zadaným id existuje.</div>
     * @param id <div class="en">id of the entity, must not be null.</div>
     *           <div class="sk">id entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting true if entity exists, false otherwise.</div>
     * <div class="sk">Mono emitujúce true ak entita existuje, false inak.</div>
     */
    Mono<Boolean> organisationUnitExistsById(String id);
}
