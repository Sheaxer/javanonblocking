package stuba.fei.gono.java.nonblocking.mongo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple5;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.mongo.repositories.*;
import stuba.fei.gono.java.nonblocking.services.*;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.nonblocking.validation.ReportedOverlimitTransactionValidator;
import stuba.fei.gono.java.pojo.State;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/***
 * <div class="en">Implementation of service that manages marshalling and de-marshalling
 * ReportedOverlimitTransaction entities using CRUD operations and ReportedOverlimitRepository instance.</div>
 * <div class="sk">Implementácia služby, ktorá spravuje marhalling a de-marshalling objektov tried
 * ReportedOverlimitTransaction použitím CRUD operácií a inštanciu rozhrania
 * ReportedOverlimitTransactionRepository.</div>
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 * @see ReportedOverlimitTransactionRepository
 */
@Slf4j
@Service
public class ReportedOverlimitTransactionServiceImpl implements ReportedOverlimitTransactionService {

    /***
     * <div class="en">Name of the sequence that stores data necessary for generating new ids.</div>
     * <div class="sk">Názov sekvencie, ktorá obsahuje dáta potrebné na generáciu nových id.</div>
     */
    @Value("${reportedOverlimitTransaction.transaction.sequenceName:customSequences}")
    private String sequenceName;

    /***
     * <div class="en">Service used to verify if createdBy property of the entity references an
     * existing Employee entity.</div>
     * <div class="sk">Služba použitá na kontrolu, či createdBy premenná entity identifikuje skutočnú entitu
     * triedy Employee.</div>
     */
    private EmployeeService employeeService;

    /***
     * <div class="en">Repository providing CRUD operations on ReportedOverlimitTransaction entities.</div>
     * <div class="sk">Repozitár poskytujúci CRUD operácie nad entitami triedy ReportedOverlimitTransaction.</div>
     */
    private ReportedOverlimitTransactionRepository transactionRepository;

    /***
     * <div class="en">Validatior used to verification if the entity is correct before saving it in PUT or
     * POST operation.</div>
     * <div class="sk">Validátor použitý na verifikáciu či je entita korektná pred jej uložením v PUT alebo POST
     * operácii.</div>
     */
    private ReportedOverlimitTransactionValidator validator;

    /***
     * <div class="en">Service used to verify if the organisationUnitID property references an existing OrganisationUnit
     * entity.</div>
     * <div class="sk">Služba použitá na verifikáciu či organisationUnitID premenná referencuje skutočnú
     * entitu triedy OrganisationUnit.</div>
     */
    private OrganisationUnitService organisationUnitService;

    /***
     * <div class="en">Service used to verify if clientId property references an existing Client entity.</div>
     * <div class="sk">Služba použitá na verifikáciu či clientId premenná referencuje skutočnú entitu
     * triedy Client.</div>
     */
    private ClientService clientService;

    /***
     * <div class="en">Service used to generate new id to save a new entity in the POST operation.</div>
     * <div class="sk">Služba použitá na generáciu nových id na uloženie entít pomocou POST operácie.</div>
     */
    private NextSequenceService nextSequenceService;

    /***
     * <div class="en">Service used to verify if the sourceAccount property references an
     * existing Account entity.</div>
     * <div class="sk"> Služba použitá na verifikáciu, či sourceAccount premenná referencuje skutočnú entitu triedy
     * Account.</div>
     */
    private AccountService accountService;

    @Autowired
    public ReportedOverlimitTransactionServiceImpl(EmployeeService employeeService,
                                                   ReportedOverlimitTransactionRepository transactionRepository,
                                                   OrganisationUnitService organisationUnitService,
                                                   ClientService clientService,
                                                   NextSequenceService nextSequenceService,
                                                   AccountService accountService,
                                                   ReportedOverlimitTransactionValidator validator) {
        this.employeeService = employeeService;
        this.transactionRepository = transactionRepository;
        this.validator = validator;
        this.organisationUnitService = organisationUnitService;
        this.clientService = clientService;
        this.accountService = accountService;
        this.nextSequenceService = nextSequenceService;
    }

    /***
     * <div class="en">Collects the error messages and throws an exception if validation failed.</div>
     * <div class="sk">Zhromaždí chybové hlášky a vyhodí výnimku ak je validácia neúspešná.</div>
     * @param x <div class="en">Tuple5 of the Monos carrying validaiton info - output from validator and output
     *          from checking if entity referenced by properties of ReportedOverlimitTransaction entity exists.</div>
     *          <div class="sk">Tuple5 Mono-v ktoré obsahujú informácie o validácii - výstup z validátora a
     *          výstupy služieb ktoré kontrolujú či entity referencované z entity triedy ReportedOverlimitTransaction
     *          naozaj existujú.</div>
     * @return <div class="en">true if validation was successful.</div>
     * <div class="sk">true ak validdácia prebehla úspešne.</div>
     * @throws ReportedOverlimitTransactionValidationException <div class="en">exception containing error messages,
     * thrown if validation failed.</div>
     * <div class="sk">výnimka obsahujúca chybové hlášky, vyvolaná ak validácia prebehla neúspešne.</div>
     * @see Tuple5
     */
    private static Boolean apply(Tuple5<Boolean, Boolean, Boolean, Boolean, Errors> x)
            throws ReportedOverlimitTransactionValidationException {
        List<String> customErrors = new ArrayList<>();
        if (!x.getT1())
            customErrors.add("CLIENTID_INVALID");
        if (!x.getT2())
            customErrors.add("ORGANISATIONUNIT_INVALID");
        if (!x.getT3())
            customErrors.add("CREATEDBY_INVALID");
        if (!x.getT4())
            customErrors.add("ACCOUNT_OFFLINE");
        /* map errors from validator to String */
        x.getT5().getAllErrors().stream().
                map(oe -> Objects.requireNonNull(oe.getCodes())[oe.getCodes().length - 1]).forEach(customErrors::add);
        if (customErrors.isEmpty()) {
            return true;
        } else
            /* validation failed, throw exception containing all the error messages */
            throw new ReportedOverlimitTransactionValidationException(customErrors);
    }

    /***
     * <div class="en">Post operation - generate new id and save the entity.</div>
     * <div class="sk">Post operácia nad entitou -vygeneruje sa nové id a entita sa uloží.</div>
     * @param transaction <div class="en">entity to be saved.</div>
     *                    <div class="sk">entita ktorá sa má uložiť.</div>
     * @return <div class="en">Mono emitting the saved entity.</div>
     * <div class="sk">Mono emitujúce uloženú entitu.</div>
     * @throws ReportedOverlimitTransactionValidationException <div class="en">exception containing error codes
     * thrown if entity is not valid.</div>
     * <div class="sk">výnimka obsahujúca chybové kódy vyvolaná ak validácia entity prebehne neúspešne.</div>
     */
    @Override
    public Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction)
    throws ReportedOverlimitTransactionValidationException{
        return Mono.just(transaction).flatMap( t-> {
            t.setModificationDate(OffsetDateTime.now());
            t.setState(State.CREATED);
            return validate(t).then(nextSequenceService.getNewId(transactionRepository, sequenceName).flatMap(
                    newId ->
                    {
                        t.setId(newId);
                        return transactionRepository.save(t);
                    }
            ).cast(ReportedOverlimitTransaction.class));
        });
    }

    /***
     * <div class="en">Finds the entity with the given id.</div>
     * <div class="sk">Nájde entitu so zadaným id.</div>
     * @param id <div class="en">must not be null.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if there is none.</div>
     * <div class="sk">Mono emitujúce hľadanú entitu alebo Mono.empty() ak neexistuje.</div>
     * @see ReportedOverlimitTransactionValidationException
     */
    @Override
    public Mono<ReportedOverlimitTransaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    /***
     * <div class="en">Validates the entity and if valid - saves it using the given id. Notifies
     * the NextSequenceService to check if the id used is a new maximal id. Sets the modification date
     * to the time of saving and if entity with the given id did not exist before, sets the state to State.CREATED.
     * </div>
     * <div class="sk">Validuje entitu a ak je korektná - uloží ju so zadaným id. Notifikuje
     * NextSequenceService inštanciu, aby skontrolova, či použité id je nová maximálna hodnota.
     * Nastaví dátum modifikácie na dátum uloženia a ak entita so zadaným id predtým neexistovala,
     * nastaví stav na State.CREATED.</div>
     * @param id <div class="en">id that will identify the saved entity.</div>
     *           <div class="sk">id ktoré bude identifikovať uloženú entitu.</div>
     * @param transaction <div class="en">entity to be saved.</div>
     *                    <div class="sk">entita, ktorá má byť uložená.</div>
     * @return <div class="en">Mono emitting the saved entity or
     * Mono.error() containing the ReportedOverlimitTransactionValidationException if
     * the entity is not valid.</div>
     * <div class="sk">Mono emitujúce uloženú entitu ak validácia prebehla úspešne alebo Mono.error()
     * obsahujúce výnimku ReportedOverlimitTransactionValidationException s validačnými chybami ak validácia
     * skončila neúspešne.</div>
     * @see ReportedOverlimitTransactionValidationException
     */
    @Override
    public Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction) {
        //transaction.setId(id);
        //transaction.setModificationDate(OffsetDateTime.now());
        return Mono.just(transaction).flatMap(
                t ->
                {
                    t.setId(id);
                    t.setModificationDate(OffsetDateTime.now());
                    return validate(t).then(nextSequenceService.needsUpdate(sequenceName,id)).then(
                            transactionRepository.existsById(id).flatMap(
                                    b->
                                    {
                                        if(!b)
                                        {
                                            t.setState(State.CREATED);
                                        }
                                        return transactionRepository.save(t);
                                    }
                            )
                    );
                }
        );

    }

    /***
     * <div class="en">Performs the validation of entity before saving it in either PUT or POST method.
     * Sets up a Tuple5 of monos emitting results from validator and services and calls apply method with
     * this Tuple5 as the parameter.</div>
     * <div class="sk">Vykoná validáciu entity pred jej uložením v PUT alebo POST metóde. Nastaví Tuple5 Mono-v
     * ktoré emitujú výstupy z validátora a služieb a zavolá metódu apply s týmto Tuple5</div>
     * @param transaction <div class="en">entity to be validated.</div>
     *                    <div class="sk">validovaná entita.</div>
     * @return <div class="en">mono emitting when the validation was performed.</div>
     * <div class="sk">mono emitujúce kedy validácia bola uskutočnená</div>
     * @throws ReportedOverlimitTransactionValidationException <div class="en">exception containing validation errors
     * thrown if the validation was unsuccessful .
     * </div> <div class="sk">výnimka obsahujúca chybové hlášky validácie, ktorá je vyvolaná ak je validácia
     * neúspešná.</div>
     */
    @Override
    public Mono<Void> validate(ReportedOverlimitTransaction transaction)
            throws ReportedOverlimitTransactionValidationException
    {
        Mono<Boolean> cl;
        Mono<Boolean> o;
        Mono<Boolean> emp;
        Mono<Boolean> activeAccount;
        Mono<Errors> errorsMono;

        /*
         * Checks for errors using the ReportedOverlimitTransactionValidator
         */
       errorsMono = Mono.just(transaction).flatMap(
               t->
               {
                   Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
                   validator.validate(t,errors);
                   return Mono.just(errors);
               }

       ).cast(Errors.class);
        /*
         Checks if Client referenced by cliendId field exists.
         */
        if(transaction.getClientId()!=null)
            cl = clientService.clientExistsById(transaction.getClientId());
        /* if clientId is null we don't want double error messages so we set Mono to emit true
         */
        else
            cl = Mono.just(true);
        /*
         * Checks if OrganisationUnit exists referenced by the organisationUnitID field.
         */
        if(transaction.getOrganisationUnitID() != null)
            o = organisationUnitService.organisationUnitExistsById(transaction.getOrganisationUnitID());
        /* if organisationUnitID is null we don't want double error messages so we set Mono to emit true
         */
        else
            o=Mono.just(true);
        /*
         * Checks if Employee referenced by createdBy field exists.
         */
        if(transaction.getCreatedBy()!= null)
            emp = employeeService.employeeExistsById(transaction.getCreatedBy());
        /* if createdBy is null we don't want double error messages so we set Mono to emit true */
        else
            emp = Mono.just(true);
        /* Trying to find Account  */
        if(transaction.getSourceAccount() != null)
        {
            /* Account is identified by IBAN */
            if(transaction.getSourceAccount().getIban()!= null) {
                activeAccount = accountService.getAccountByIban(transaction.getSourceAccount().getIban()).map(
                        t-> {
                            if(t.getIsActive() == null)
                                return false;
                            return t.getIsActive();
                        }
                        /* if can't find the account set Mono to emit false */
                ).switchIfEmpty(Mono.just(false));
                /* Account is identified by Local Account Number */
            } else if(transaction.getSourceAccount().getLocalAccountNumber() != null) {
                activeAccount = accountService.getAccountByLocalAccountNumber(transaction.getSourceAccount().
                        getLocalAccountNumber()).map(
                        t->
                        {
                            if(t.getIsActive() == null)
                                return false;
                            return t.getIsActive();
                        }
                /* If can't find account set Mono to emit false */
                ).switchIfEmpty(Mono.just(false));
                /* Account is not identified by either Iban or Local Account Number - set the Mono to emit false */
            } else
                activeAccount = Mono.just(false);
        }
        /* sourceAccount is null - already caught in the validator, we don't want double error messages, set
        Mono to emit
        true */
        else
            activeAccount =  Mono.just(true);
        /* zip the Monos together for validation and check*/
        Mono<Tuple5<Boolean, Boolean, Boolean, Boolean, Errors>> tup = Mono.zip(cl, o, emp,activeAccount,errorsMono);
            return tup.map(
                    /* checks for */
                    ReportedOverlimitTransactionServiceImpl::apply
            ).then();
    }

    /***
     * <div class="en">Deletes the entity with the given id.</div>
     * <div class="sk">Zmaže entitu so zadaným id.</div>
     * @param id <div class="en">must not be null.</div>
     *           <div class="sk">nesmie byť null.</div>
     * @return <div class="en">Mono emitting when the operation was completed,
     * Mono.error(ReportedOverlimitTransactionNotFoundException)
     * if the entity with given id was not found or Mono.error(ReportedOverlimitTransactionBadRequestException) if the
     * entity couldn't be deleted because its state is CLOSED. </div>
     * <div class="sk">Mono emitujúce kedy bola operácia úspešne vykonaná alebo
     * Mono.error(ReportedOverlimitTransactionBadRequestException) ak entita nemohla byť zmazaná pretože jej stav
     * bol CLOSED.</div>
     * @see ReportedOverlimitTransactionNotFoundException
     * @see ReportedOverlimitTransactionBadRequestException
     * @see State
     * @see ReportedOverlimitTransaction
     */
    @Override
    public Mono<Void> deleteTransaction(String id)
    {
      return getTransactionById(id).switchIfEmpty(
              Mono.error(new ReportedOverlimitTransactionNotFoundException("ID_NOT_FOUND"))).flatMap(
                      t ->
                      {
                          if(t.getState().equals(State.CLOSED))
                              return transactionRepository.deleteById(id);
                          else
                              return Mono.error(
                                      new ReportedOverlimitTransactionBadRequestException("STATE_CLOSED")
                              );
                      }
      );

    }
}
