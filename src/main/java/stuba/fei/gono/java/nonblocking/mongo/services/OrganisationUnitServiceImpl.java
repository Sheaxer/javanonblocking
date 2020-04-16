package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.services.OrganisationUnitService;
import stuba.fei.gono.java.pojo.OrganisationUnit;

@Service
public class OrganisationUnitServiceImpl  implements OrganisationUnitService {

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
