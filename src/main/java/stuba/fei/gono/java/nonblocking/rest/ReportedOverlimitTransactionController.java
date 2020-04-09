package stuba.fei.gono.java.nonblocking.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;

@Slf4j
@RestController
//@RequestMapping(value = "/reportedOverlimitTransaction")
public class ReportedOverlimitTransactionController {

    private ReportedOverlimitTransactionService transactionService;

    @Autowired
    public ReportedOverlimitTransactionController(ReportedOverlimitTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @GetMapping(value = "/reportedOverlimitTransaction/{id}")
    @ResponseBody
    public Mono<ResponseEntity> getTransaction (@PathVariable String id)
    {
        //return transactionRepository.findById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_INVALID")));
        return transactionService.getTransactionById(id).map(
                t -> ResponseEntity.status(HttpStatus.OK).body(t)
        ).cast(ResponseEntity.class);
    }

    @PostMapping(value = "/reportedOverlimitTransaction", consumes = "application/json")
    public Mono<ResponseEntity> postTransaction( @RequestBody ReportedOverlimitTransaction newTransaction)
    {

        /*Errors errors = new BeanPropertyBindingResult(newTransaction, ReportedOverlimitTransaction.class.getName());
        validator.validate(newTransaction,errors);
        if(errors == null || errors.getAllErrors().isEmpty())
        {

            Mono<Client> cl =clientRepository.findById(newTransaction.getClientId()).switchIfEmpty(Mono.just(new Client()));
            Mono<OrganisationUnit> o = organisationUnitRepository.findById(newTransaction.getOrganisationUnitID()).
                    switchIfEmpty(Mono.just(new OrganisationUnit()));
            Mono<Employee> emp = employeeRepository.findById(newTransaction.getCreatedBy()).
                    switchIfEmpty(Mono.just(new Employee()));

            Mono<ResponseEntity> trans = transactionRepository.save(newTransaction).map(t ->
            {ResponseEntity<ReportedOverlimitTransaction> x
                    = ResponseEntity.status(HttpStatus.OK).body(t); return x;}).cast(ResponseEntity.class);



           Mono<Tuple3<Client,OrganisationUnit,Employee>> tup= Mono.zip(cl,o,emp);

            return tup.map(
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

            ).then(trans).onErrorResume(throwable -> throwable instanceof ReportedOverlimitTransactionException,
                    throwable -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(throwable.getMessage())));
        }

            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    errors.getAllErrors().stream().map(t -> t.getCodes()[t.getCodes().length-1]).collect(Collectors.toList())));

         */

      return  transactionService.postTransaction(newTransaction).map(
                t->
                        ResponseEntity.status(HttpStatus.OK).body(t)
        ).cast(ResponseEntity.class).onErrorResume(throwable -> throwable instanceof ReportedOverlimitTransactionValidationException,
              throwable -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(((ReportedOverlimitTransactionValidationException)throwable).getErrors())));
    }

}
