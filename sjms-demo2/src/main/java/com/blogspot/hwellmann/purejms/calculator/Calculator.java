package com.blogspot.hwellmann.purejms.calculator;

import java.util.concurrent.Future;

public interface Calculator {

    Future<Integer> add(int op1, int op2);
}
