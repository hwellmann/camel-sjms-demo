/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blogspot.hwellmann.purejms.calculator.web;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blogspot.hwellmann.purejms.calculator.Calculator;

/**
 * @author Harald Wellmann
 * 
 */
@RequestScoped
@Named("calculator")
public class CalculatorController {

    @Inject
    private Operands operands;

    @Inject
    @Named("calculatorClient")
    private Calculator calculator;

    private Integer sum;

    public void add() {
        Future<Integer> result = calculator.add(operands.getOp1(), operands.getOp2());
        try {
            sum = (result == null) ? null : result.get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Integer getSum() {
        return sum;
    }

}
