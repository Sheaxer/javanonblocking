package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.services.OrganisationUnitService;
import stuba.fei.gono.java.pojo.OrganisationUnit;

/***
 * <div class="en">Implementation of service that manages marshalling and de-marshalling
 * OrganisationUnit entities using CRUD operations and OrganisationUnitRepository instance.</div>
 * <div class="sk">Implementácia služby ktorá spravuje marshalling a de-marshalling entít triedy OrganisationUnit
 * pomocou CRUD operácií a inštanciou rozhrania OrganisationUnitRepository. </div>
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 * @see OrganisationUnitRepository
 */
@Service
public class OrganisationUnitServiceImpl  implements OrganisationUnitService {

    /***
     * <div class="en">Repository that provides CRUD operations on OrganisationUnit entities.</div>
     * <div class="sk">Repozitár ktorý poskytuje CRUD operácie nad entitami triedy OrganisationUnit.</div>
     * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
     */
    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitServiceImpl(OrganisationUnitRepository organisationUnitRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
    }


    @Override
    public Mono<OrganisationUnit> getOrganisationUnitById(String id) {
        return organisationUnitRepository.findById(id);
    }


    @Override
    public Mono<Boolean> organisationUnitExistsById(String id) {
        return organisationUnitRepository.existsById(id);
    }
}
