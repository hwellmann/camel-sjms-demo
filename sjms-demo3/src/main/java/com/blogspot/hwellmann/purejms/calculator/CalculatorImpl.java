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

    @Override
    public Future<Integer> add(int op1, int op2) {
        System.out.println("processing add: " + textBean.getText());
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int result = op1 + op2;
        System.out.println("result = " + result);
        return new Result<Integer>(result);
    }

}
