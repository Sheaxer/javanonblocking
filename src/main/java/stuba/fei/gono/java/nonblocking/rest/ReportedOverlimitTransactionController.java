package stuba.fei.gono.java.nonblocking.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ReportedOverlimitTransactionRepository;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/reportedOverlimitTransaction")
public class ReportedOverlimitTransactionController {

    ReportedOverlimitTransactionRepository transactionRepository;

    @Autowired
    public ReportedOverlimitTransactionController(ReportedOverlimitTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Mono<ReportedOverlimitTransaction> getTransaction (@PathVariable String id)
    {
        return transactionRepository.findById(id);
    }
}
