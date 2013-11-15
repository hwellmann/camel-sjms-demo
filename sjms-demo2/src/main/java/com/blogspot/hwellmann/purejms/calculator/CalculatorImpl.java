package com.blogspot.hwellmann.purejms.calculator;

import java.util.concurrent.Future;

import javax.ejb.Stateless;

@Stateless
public class CalculatorImpl implements Calculator {

    @Override
    public Future<Integer> add(int op1, int op2) {
        System.out.println("processing add");
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
