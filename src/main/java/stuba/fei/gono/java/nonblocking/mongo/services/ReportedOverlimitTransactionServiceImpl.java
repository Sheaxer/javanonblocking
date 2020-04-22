package stuba.fei.gono.java.nonblocking.mongo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ReportedOverlimitTransactionRepository;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.nonblocking.validation.ReportedOverlimitTransactionValidator;

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

    @Autowired
    public ReportedOverlimitTransactionServiceImpl(EmployeeRepository employeeRepository,
                                                   ReportedOverlimitTransactionRepository transactionRepository,
                                                   OrganisationUnitRepository organisationUnitRepository,
                                                   ClientRepository clientRepository,
                                                   NextSequenceService nextSequenceService,
                                                   ReportedOverlimitTransactionValidator validator) {
        this.employeeRepository = employeeRepository;
        this.transactionRepository = transactionRepository;
        this.validator = validator;
        this.organisationUnitRepository = organisationUnitRepository;
        this.clientRepository = clientRepository;
        this.nextSequenceService = nextSequenceService;
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
        return transactionRepository.findById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_NOT_FOUND")));
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
       Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
        validator.validate(transaction,errors);
        if(errors.getAllErrors().isEmpty()) {

            Mono<Boolean> cl = clientRepository.existsById(transaction.getClientId());
            Mono<Boolean> o = organisationUnitRepository.existsById(transaction.getOrganisationUnitID());
            Mono<Boolean> emp = employeeRepository.existsById(transaction.getCreatedBy());

            Mono<Tuple3<Boolean, Boolean, Boolean>> tup = Mono.zip(cl, o, emp);

            return tup.map(
                    x ->
                    {
                        List<String> customErrors = new ArrayList<>();
                        if (!x.getT1())
                            customErrors.add("CLIENTID_NOT_VALID");
                        if (!x.getT2())
                            customErrors.add("ORGANISATIONUNIT_NOT_VALID");
                        if (!x.getT3())
                            customErrors.add("CREATEDBY_NOT_VALID");

                        if (customErrors.isEmpty()) {
                            return true;
                        } else
                            throw new ReportedOverlimitTransactionValidationException(customErrors);
                    }

            ).then();
        }
        else
        {
            return Mono.error( new ReportedOverlimitTransactionValidationException(errors.getAllErrors().stream().map(
                    t->
                            Objects.requireNonNull(t.getCodes())[t.getCodes().length-1]

            ).collect(Collectors.toList())));
        }

    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
      return getTransactionById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_NOT_FOUND"))).
              then(transactionRepository.deleteById(id));
    }
}
