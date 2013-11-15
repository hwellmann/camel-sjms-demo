package com.blogspot.hwellmann.purejms.calculator;

import java.util.concurrent.Future;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Stateless
@Named("calculatorImpl")
public class CalculatorImpl implements Calculator {

    @Inject
    private TextBean textBean;

    @Inject
    @Named("calculatorDelegate")
    private Calculator delegate;

    @Override
    public Future<Integer> add(int op1, int op2) {

        textBean.setText(String.format("%d + %d", op1, op2));
        return delegate.add(op1, op2);
    }

}
