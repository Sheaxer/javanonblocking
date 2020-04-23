package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.pojo.Account;
import stuba.fei.gono.java.pojo.OrganisationUnit;
/***
 * Interface extending ReactiveMongoRepository for OrganisationUnit entities.
 * @see OrganisationUnit
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface OrganisationUnitRepository extends ReactiveCrudRepository<OrganisationUnit, String> {
}
