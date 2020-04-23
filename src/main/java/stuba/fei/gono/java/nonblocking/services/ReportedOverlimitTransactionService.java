package stuba.fei.gono.java.nonblocking.services;


import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;


public interface ReportedOverlimitTransactionService {

     Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction) throws
             ReportedOverlimitTransactionValidationException;
     Mono<ReportedOverlimitTransaction> getTransactionById(String id);
     Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction) throws
             ReportedOverlimitTransactionValidationException;
     Mono<Void> deleteTransaction (String id) throws ReportedOverlimitTransactionNotFoundException,
             ReportedOverlimitTransactionBadRequestException;

}
