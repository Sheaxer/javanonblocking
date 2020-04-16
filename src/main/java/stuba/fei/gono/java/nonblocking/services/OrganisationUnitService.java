package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.OrganisationUnit;

public interface OrganisationUnitService {

    Mono<OrganisationUnit> getOrganisationUnitById(String id);

    Mono<Boolean> organisationUnitExistsById(String id);
}
