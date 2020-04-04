package stuba.fei.gono.java.nonblocking.rest;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionException;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ReportedOverlimitTransactionRepository;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.validation.ReportedOverlimitTransactionValidator;

import javax.validation.Valid;
import javax.validation.ValidationException;
@Slf4j
@RestController
//@RequestMapping(value = "/reportedOverlimitTransaction")
public class ReportedOverlimitTransactionController {

    ReportedOverlimitTransactionRepository transactionRepository;
    ReportedOverlimitTransactionValidator validator;

    @Autowired
    public ReportedOverlimitTransactionController(ReportedOverlimitTransactionRepository transactionRepository,
                                                  ReportedOverlimitTransactionValidator validator) {
        this.transactionRepository = transactionRepository;
        this.validator = validator;
    }

    @GetMapping(value = "/reportedOverlimitTransaction/{id}")
    @ResponseBody
    public Mono<ReportedOverlimitTransaction> getTransaction (@PathVariable String id)
    {
        return transactionRepository.findById(id).switchIfEmpty(Mono.error(new ReportedOverlimitTransactionException("ID_INVALID")));
    }

    @PostMapping(value = "/reportedOverlimitTransaction", consumes = "application/json")
    public Mono<ResponseEntity> postTransaction(@Valid @RequestBody ReportedOverlimitTransaction newTransaction)
    {
        /*if(result.hasErrors())
        {
            log.info("AAA");
        }*/
        //ReportedOverlimitTransaction newTransaction = monoTransaction.block();
        //newTransaction = newTransaction.map( t-> {t.setId("zirgon"); return t;});
        //newTransaction.setId("A");
        log.info("jes");
         /*Mono<String> responseBody = Mono.just(newTransaction).map(
                body -> {
                    Errors errors = new BeanPropertyBindingResult(body, ReportedOverlimitTransaction.class.getName());
                    validator.validate(body,errors);
                    log.info("HIJA");
                    if(errors == null || errors.getAllErrors().isEmpty())
                    {
                        return Mono.just(body.toString());
                    }
                    else
                    {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
                    }
                }
        );*/
         //transactionRepository.saveAll(newTransaction);


        return transactionRepository.save(newTransaction).map(t ->
                    {ResponseEntity<ReportedOverlimitTransaction> x
                         = ResponseEntity.status(HttpStatus.OK).body(t); return x;}).cast(ResponseEntity.class);

        /*return newTransaction.map(t -> {t.setId( "45623"); transactionRepository.save(t);
            ResponseEntity<ReportedOverlimitTransaction> x = ResponseEntity.status(HttpStatus.OK).body(t); return x;
        }).cast(ResponseEntity.class);*/

    }

}
