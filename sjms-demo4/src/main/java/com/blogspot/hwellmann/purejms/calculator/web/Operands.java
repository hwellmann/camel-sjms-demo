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

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Harald Wellmann
 * 
 */
@RequestScoped
@Named("operands")
public class Operands {

    private int op1;
    private int op2;

    /**
     * @return the op1
     */
    public int getOp1() {
        return op1;
    }

    /**
     * @param op1
     *            the op1 to set
     */
    public void setOp1(int op1) {
        this.op1 = op1;
    }

    /**
     * @return the op2
     */
    public int getOp2() {
        return op2;
    }

    /**
     * @param op2
     *            the op2 to set
     */
    public void setOp2(int op2) {
        this.op2 = op2;
    }

}
