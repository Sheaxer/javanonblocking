package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.pojo.OrganisationUnit;
@Repository
public interface OrganisationUnitRepository extends ReactiveCrudRepository<OrganisationUnit, String> {
}
