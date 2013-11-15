package com.blogspot.hwellmann.purejms.calculator;

import java.util.concurrent.Executor;

import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@Local(AsyncExecutor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class AsyncExecutor implements Executor {

    @Override
    @Asynchronous
    public void execute(Runnable command) {
        command.run();
    }
}
