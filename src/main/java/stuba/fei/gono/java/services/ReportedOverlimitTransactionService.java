package stuba.fei.gono.java.services;


import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;


public interface ReportedOverlimitTransactionService {

     ReportedOverlimitTransaction postTransaction(ReportedOverlimitTransaction transaction);
     ReportedOverlimitTransaction getTransactionById(String id);
     ReportedOverlimitTransaction putTransaction(String id, ReportedOverlimitTransaction transaction);
     ReportedOverlimitTransaction deleteTransaction (String id);

}
