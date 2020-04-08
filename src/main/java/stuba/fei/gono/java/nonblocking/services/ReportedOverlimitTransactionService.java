package stuba.fei.gono.java.nonblocking.services;


import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;


public interface ReportedOverlimitTransactionService {

     Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction);
     Mono<ReportedOverlimitTransaction> getTransactionById(String id);
     Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction);
     Mono<ReportedOverlimitTransaction> deleteTransaction (String id);

}