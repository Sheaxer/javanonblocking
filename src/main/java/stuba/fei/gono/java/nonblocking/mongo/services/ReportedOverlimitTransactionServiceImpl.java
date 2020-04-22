package stuba.fei.gono.java.nonblocking.mongo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple5;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.mongo.repositories.*;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.nonblocking.validation.ReportedOverlimitTransactionValidator;
import stuba.fei.gono.java.pojo.Account;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private static Boolean apply(Tuple5<Boolean, Boolean, Boolean, Account, Errors> x) {
        List<String> customErrors = new ArrayList<>();
        if (!x.getT1())
            customErrors.add("CLIENTID_NOT_VALID");
        if (!x.getT2())
            customErrors.add("ORGANISATIONUNIT_NOT_VALID");
        if (!x.getT3())
            customErrors.add("CREATEDBY_NOT_VALID");
                        /*if(x.getT5() == null)
                            customErrors.add("ACCOUNT_OFFLINE");*/
        if (x.getT4().getIsActive() == null || !x.getT4().getIsActive())
            customErrors.add("ACCOUNT_OFFLINE");
        x.getT5().getAllErrors().stream().
                map(oe -> Objects.requireNonNull(oe.getCodes())[oe.getCodes().length - 1]).forEach(customErrors::add);
        if (customErrors.isEmpty()) {
            return true;
        } else
            throw new ReportedOverlimitTransactionValidationException(customErrors);
    }


    @Override
    public Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction) {
        transaction.setModificationDate(OffsetDateTime.now());
        //transaction.setZoneOffset(OffsetDateTime.now().getOffset().getId());

        return  test(transaction).then(nextSequenceService.getNewId(transactionRepository,sequenceName).flatMap(
                newId ->
                {
                    transaction.setId(newId);
                    return transactionRepository.save(transaction);
                }
        ).cast(ReportedOverlimitTransaction.class));
    }

    @Override
    public Mono<ReportedOverlimitTransaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction) {
        transaction.setId(id);
        transaction.setModificationDate(OffsetDateTime.now());
        //transaction.setZoneOffset(OffsetDateTime.now().getOffset().getId());
        return test(transaction).then(transactionRepository.save(transaction));


    }

    private Mono<Void> test(ReportedOverlimitTransaction transaction)
    {
        Mono<Boolean> cl;
        Mono<Boolean> o;
        Mono<Boolean> emp;
        Mono<Account> activeAccount;
        Mono<Errors> errorsMono;
        Account a = new Account();
        a.setIsActive(true);
       // Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
       errorsMono = Mono.just(transaction).flatMap(
               t->
               {
                   Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
                   validator.validate(t,errors);
                   return Mono.just(errors);
               }

       ).cast(Errors.class);
        //validator.validate(transaction,errors);
        //if(errors.getAllErrors().isEmpty()) {

            if(transaction.getClientId()!=null)
                cl = clientRepository.existsById(transaction.getClientId());
            else
                cl = Mono.just(true);
            if(transaction.getOrganisationUnitID() != null)
                o = organisationUnitRepository.existsById(transaction.getOrganisationUnitID());
            else
                o=Mono.just(true);
            if(transaction.getCreatedBy()!= null)
                emp = employeeRepository.existsById(transaction.getCreatedBy());
            else
                emp = Mono.just(true);
            if(transaction.getSourceAccount() != null)
            {
                if(transaction.getSourceAccount().getIban()!= null)
                    activeAccount = accountRepository.findAccountByIban(transaction.getSourceAccount().getIban());
                else if(transaction.getSourceAccount().getLocalAccountNumber() != null)
                    activeAccount = accountRepository.findAccountByLocalAccountNumber(transaction.getSourceAccount().getLocalAccountNumber());
                else
                    activeAccount = Mono.just(new Account());
            }
            else
                activeAccount =  Mono.just(a);
          //  Mono<Tuple4<Boolean, Boolean, Boolean, Account>> tup = Mono.zip(cl, o, emp,activeAccount);
        Mono<Tuple5<Boolean, Boolean, Boolean, Account, Errors>> tup = Mono.zip(cl, o, emp,activeAccount,errorsMono);
            return tup.map(
                    /*if(x.getT5() == null)
                            customErrors.add("ACCOUNT_OFFLINE");*/
                    ReportedOverlimitTransactionServiceImpl::apply

            ).then();
       /* }
        else
        {
            return Mono.error( new ReportedOverlimitTransactionValidationException(errors.getAllErrors().stream().map(
                    t->
                            Objects.requireNonNull(t.getCodes())[t.getCodes().length-1]

            ).collect(Collectors.toList())));
        }*/

    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
      return getTransactionById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_NOT_FOUND"))).
              then(transactionRepository.deleteById(id));
    }
}
