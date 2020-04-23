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
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.nonblocking.validation.ReportedOverlimitTransactionValidator;
import stuba.fei.gono.java.pojo.State;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/***
 * MongoDB implementation of service that handles marshalling and de-marshalling ReportedOverlimitTransaction objects.
 */
@Slf4j
@Service
public class ReportedOverlimitTransactionServiceImpl implements ReportedOverlimitTransactionService {

    @Value("${reportedOverlimitTransaction.transaction.sequenceName:customSequences}")
    private String sequenceName;

    private EmployeeRepository employeeRepository;
    private ReportedOverlimitTransactionRepository transactionRepository;
    private ReportedOverlimitTransactionValidator validator;
    private OrganisationUnitRepository organisationUnitRepository;
    private ClientRepository clientRepository;
    private NextSequenceService nextSequenceService;
    private AccountRepository accountRepository;

    @Autowired
    public ReportedOverlimitTransactionServiceImpl(EmployeeRepository employeeRepository,
                                                   ReportedOverlimitTransactionRepository transactionRepository,
                                                   OrganisationUnitRepository organisationUnitRepository,
                                                   ClientRepository clientRepository,
                                                   NextSequenceService nextSequenceService,
                                                   AccountRepository accountRepository,
                                                   ReportedOverlimitTransactionValidator validator) {
        this.employeeRepository = employeeRepository;
        this.transactionRepository = transactionRepository;
        this.validator = validator;
        this.organisationUnitRepository = organisationUnitRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.nextSequenceService = nextSequenceService;
    }

    /***
     * Adds error messages and throws Exception if validation failed.
     * @param x - Tuple 5 of the Monos carrying validaiton info.
     * @return true if validation was successful.
     * @throws ReportedOverlimitTransactionValidationException containing error messages if validation failed.
     */
    private static Boolean apply(Tuple5<Boolean, Boolean, Boolean, Boolean, Errors> x)
            throws ReportedOverlimitTransactionValidationException {
        List<String> customErrors = new ArrayList<>();
        if (!x.getT1())
            customErrors.add("CLIENTID_NOT_VALID");
        if (!x.getT2())
            customErrors.add("ORGANISATIONUNIT_NOT_VALID");
        if (!x.getT3())
            customErrors.add("CREATEDBY_NOT_VALID");
                        /*if(x.getT5() == null)
                            customErrors.add("ACCOUNT_OFFLINE");*/
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
     * Post transaction - generate new id and save it.
     * @param transaction - ReportedOverlimitTransaction to be saved.
     * @return Mono emitting the saved ReportedOverlimitTransaction.
     */
    @Override
    public Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction) {
        transaction.setModificationDate(OffsetDateTime.now());

        return  test(transaction).then(nextSequenceService.getNewId(transactionRepository,sequenceName).flatMap(
                newId ->
                {
                    transaction.setId(newId);
                    return transactionRepository.save(transaction);
                }
        ).cast(ReportedOverlimitTransaction.class));
    }

    /***
     * Return entity with the given id.
     * @param id must not be null.
     * @return Mono emitting the entity or Mono.empty() if there is none.
     * @see ReportedOverlimitTransactionValidationException
     */
    @Override
    public Mono<ReportedOverlimitTransaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction) {
        transaction.setId(id);
        transaction.setModificationDate(OffsetDateTime.now());
        //transaction.setZoneOffset(OffsetDateTime.now().getOffset().getId());
        return test(transaction).then(nextSequenceService.needsUpdate(sequenceName,id)).
                then(transactionRepository.save(transaction));
    }

    /***
     * Performs the validation of ReportedOverlimitTransaction.
     * @param transaction - ReportedOverlimitTransaction to be validated.
     * @return Mono emitting when the validation was performed.
     * @throws ReportedOverlimitTransactionValidationException exception containing validation errors.
     */
    private Mono<Void> test(ReportedOverlimitTransaction transaction)
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
            cl = clientRepository.existsById(transaction.getClientId());
        /* if clientId is null we don't want double error messages so we set Mono to emit true
         */
        else
            cl = Mono.just(true);
        /*
         * Checks if OrganisationUnit exists referenced by the organisationUnitID field.
         */
        if(transaction.getOrganisationUnitID() != null)
            o = organisationUnitRepository.existsById(transaction.getOrganisationUnitID());
        /* if organisationUnitID is null we don't want double error messages so we set Mono to emit true
         */
        else
            o=Mono.just(true);
        /*
         * Checks if Employee referenced by createdBy field exists.
         */
        if(transaction.getCreatedBy()!= null)
            emp = employeeRepository.existsById(transaction.getCreatedBy());
        /* if createdBy is null we don't want double error messages so we set Mono to emit true */
        else
            emp = Mono.just(true);
        /* Trying to find Account  */
        if(transaction.getSourceAccount() != null)
        {
            /* Account is identified by IBAN */
            if(transaction.getSourceAccount().getIban()!= null) {
                activeAccount = accountRepository.findAccountByIban(transaction.getSourceAccount().getIban()).map(
                        t-> {
                            if(t.getIsActive() == null)
                                return false;
                            return t.getIsActive();
                        }
                        /* if can't find the account set Mono to emit false */
                ).switchIfEmpty(Mono.just(false));
                /* Account is identified by Local Account Number */
            } else if(transaction.getSourceAccount().getLocalAccountNumber() != null) {
                activeAccount = accountRepository.findAccountByLocalAccountNumber(transaction.getSourceAccount().getLocalAccountNumber()).map(
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
        /* sourceAccount is null - already caught in the validator, we don't want double error messages, set Mono to emit
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
     * Deletes the ReportedOverlimitTransaction with the given id.
     * @param id must not be null.
     * @return Mono emitting when the operation was completed, Mono.error(ReportedOverlimitTransactionNotFoundException)
     * if the entity with given id was not found or Mono.error(ReportedOverlimitTransactionBadRequestException) if the
     * entity couldn't be deleted because its state is CLOSED.
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
