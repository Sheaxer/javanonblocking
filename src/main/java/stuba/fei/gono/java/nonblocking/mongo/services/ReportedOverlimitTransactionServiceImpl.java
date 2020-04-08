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
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ReportedOverlimitTransactionRepository;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.pojo.Client;
import stuba.fei.gono.java.pojo.Employee;
import stuba.fei.gono.java.pojo.OrganisationUnit;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.validation.ReportedOverlimitTransactionValidator;

import java.util.ArrayList;
import java.util.List;
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
        Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
        validator.validate(transaction,errors);

        if(errors == null || errors.getAllErrors().isEmpty())
        {

            return  test(transaction).then(nextSequenceService.getNewId(transactionRepository,sequenceName).flatMap(
                    newId ->
                    {
                        transaction.setId(newId);
                        return transactionRepository.save(transaction);
                    }
            ).cast(ReportedOverlimitTransaction.class));
        }
        else
        {
            throw new ReportedOverlimitTransactionException(errors.getAllErrors().stream().map(
                    t->
                            t.getCodes()[t.getCodes().length-1]

            ).collect(Collectors.toList()).toString());
        }
    }

    @Override
    public Mono<ReportedOverlimitTransaction> getTransactionById(String id) {

        return transactionRepository.findById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_NOT_FOUND")));



    }

    @Override
    public Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction) {
        transaction.setId(id);
        Errors errors = new BeanPropertyBindingResult(transaction, ReportedOverlimitTransaction.class.getName());
        validator.validate(transaction,errors);

        if(errors == null || errors.getAllErrors().isEmpty())
        {

                  return  test(transaction).then(transactionRepository.save(transaction));
        }
        else
        {
            throw new ReportedOverlimitTransactionException(errors.getAllErrors().stream().map(
                    t->
                            t.getCodes()[t.getCodes().length-1]

            ).collect(Collectors.toList()).toString());
        }


    }

    private Mono<Object> test(ReportedOverlimitTransaction transaction)
    {
        Mono<Client> cl =clientRepository.findById(transaction.getClientId()).switchIfEmpty(Mono.just(new Client()));
        Mono<OrganisationUnit> o = organisationUnitRepository.findById(transaction.getOrganisationUnitID()).
                switchIfEmpty(Mono.just(new OrganisationUnit()));
        Mono<Employee> emp = employeeRepository.findById(transaction.getCreatedBy()).
                switchIfEmpty(Mono.just(new Employee()));

        Mono<Tuple3<Client,OrganisationUnit,Employee>> tup= Mono.zip(cl,o,emp);
        Mono<Object> test = tup.map(
                x ->
                {
                    List<String> customErrors = new ArrayList<>();
                    if(x.getT1().getId() == null)
                        customErrors.add("CLIENTID_NOT_VALID");
                    if(x.getT2().getId() == null)
                        customErrors.add("ORGANISATIONUNIT_NOT_VALID");
                    if(x.getT3().getId() == null)
                        customErrors.add("CREATEDBY_NOT_VALID");

                    if(customErrors.isEmpty())
                    {
                        return Mono.empty();
                    }
                    else
                        throw new ReportedOverlimitTransactionException(customErrors.toString());
                }

        );
        return test;
    }

    @Override
    public Mono<ReportedOverlimitTransaction> deleteTransaction(String id) {
        return null;
    }
}
