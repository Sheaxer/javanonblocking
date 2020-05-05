package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

/***
 * <div class="en">Interface for marshalling and de-marshalling Employee entities.</div>
 * <div class="sk">Rozhranie pre marshalling a de-marshalling entít triedy Employee.</div>
 */
public interface EmployeeService {
    /***
     * <div class="en">Finds the entity identified by the given user name.</div>
     * <div class="sk">Nájde entitu idenfitikovanú zadaným používateľskym menom.</div>
     * @param userName <div class="en">user name identifying the entity.</div>
     *                 <div class="sk">používateľské meno identifikujúce entitu.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity was found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak entita nebola nájdená.</div>
     */
    Mono<Employee> findEmployeeByUsername(String userName);
    /***
     * <div class="en">Validates the entity and if it is valid, saves it.</div>
     * <div class="sk">Validuje entitu a ak je korektná, uloží ju.</div>
     * @param employee entity to be saved.
     * @return <div class="en">Mono emitting the saved entity if validation was successful or Mono.error() containing
     * ReportedOverlimitTransactionValidationException with the failed validation error codes.</div>
     * <div class="sk">Mono emitujúce uloženú entitu ak validácia bola úspešná alebo Mono.error() obsahujúce
     * ReportedOverlimitTransactionValidationException výnimku ktorá pozostáva z validačných chybových hlášok,
     * ak validácia skončila neúspešne.</div>
     */
    Mono<Employee> saveEmployee(Employee employee);
    /***
     * <div class="en">Checks if the entity with the given id exists.</div>
     * <div class="sk">Skontroluje či entita so zadaným id existuje.</div>
     * @param id <div class="en">id of the entity, must not be null.</div>
     *           <div class="sk">id entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting true if the entity was found, false otherwise.</div>
     * <div class="sk">Mono emitujúce true ak entita bola nájdená, inak false.</div>
     */
    Mono<Boolean> employeeExistsById(String id);

    /***
     * <div class="en">Validates the entity.</div>
     * <div class="sk">Validuje zadanú entitu.</div>
     * @param employee <div class="en">entity to be validated.</div>
     *                 <div class="sk">entita, ktorú treba validovať.</div>
     * @return <div class="en">Mono emitting that the operation was successful if the entity was valid
     * or Mono.error() containing
     * ReportedOverlimitTransactionValidationException with the failed validation error codes.</div>
     * <div class="sk">Mono emitujúce že operácia bola úspešná ak entita je korektná alebo
     * Mono.error() obsahujúce
     * ReportedOverlimitTransactionValidationException výnimku ktorá pozostáva z validačných chybových hlášok,
     * ak validácia skončila neúspešne.</div>
     */
    Mono<Void> validate(Employee employee);

    /***
     * <div class="en">Checks if entity with the given user name exists.</div>
     * <div class="sk">Skontroluje, či entita so zadaným používateľským menom existuje.</div>
     * @param username <div class="en">user name of the entity.</div>
     *                 <div class="sk">používateľské meno entity.</div>
     * @return <div class="en">Mono emitting true if the entity exists, false otherwise.</div>
     * <div class="sk">Mono emitujúce true ak entita existuje, false ak neexistuje.</div>
     */
    Mono<Boolean> employeeExistsByUsername(String username);

}
