package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.services.OrganisationUnitService;
import stuba.fei.gono.java.pojo.OrganisationUnit;

/*** MongoDB implementation of service that handles marshalling and de-marshalling  OrganisationUnit objects.
 *
 */
@Service
public class OrganisationUnitServiceImpl  implements OrganisationUnitService {

    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitServiceImpl(OrganisationUnitRepository organisationUnitRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
    }

    /***
     * Retrieves the OrganisationUnit with given id.
     * @param id - must not be null
     * @return Mono emitting the OrganisationUnit or Mono.empty() if none found.
     */
    @Override
    public Mono<OrganisationUnit> getOrganisationUnitById(String id) {
        return organisationUnitRepository.findById(id);
    }

    /***
     * Checks if OrganisationUnit with given id exists.
     * @param id - must not be null.
     * @return Mono emitting OrganisationUnit with the given id or Mono.empty() if none found.
     */
    @Override
    public Mono<Boolean> organisationUnitExistsById(String id) {
        return organisationUnitRepository.existsById(id);
    }
}
