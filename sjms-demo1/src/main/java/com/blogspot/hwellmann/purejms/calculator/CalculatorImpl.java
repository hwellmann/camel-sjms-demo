package com.blogspot.hwellmann.purejms.calculator;

import javax.ejb.Stateless;

@Stateless
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int op1, int op2) {
        return op1 + op2;
    }

}
