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
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> getTransaction (@PathVariable String id)
    {
        return transactionService.getTransactionById(id);
    }

    @PostMapping(value = "/reportedOverlimitTransaction", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> postTransaction( @RequestBody ReportedOverlimitTransaction newTransaction)
    {

      return  transactionService.postTransaction(newTransaction);
              /*.onErrorResume(throwable -> throwable instanceof ReportedOverlimitTransactionValidationException,
              throwable -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                      ((ReportedOverlimitTransactionValidationException)throwable).getErrors()))
      );*/
    }

    @PutMapping(value = "/reportedOverlimitTransaction/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> putTransaction(@PathVariable String id, @RequestBody ReportedOverlimitTransaction transaction)
    {
        return transactionService.putTransaction(id, transaction);
        /*.onErrorResume(
                throwable -> throwable instanceof ReportedOverlimitTransactionValidationException,
                throwable -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ((ReportedOverlimitTransactionValidationException)throwable).getErrors()))
        );*/
    }

    @DeleteMapping(value = "/reportedOverlimitTransaction/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTransaction(@PathVariable String id)
    {
       return  transactionService.deleteTransaction(id);
    }

}
