/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 */
package com.sleepycat.je.rep.dual.persist.test;

import java.util.List;

import org.junit.runners.Parameterized.Parameters;


public class OperationTest extends com.sleepycat.persist.test.OperationTest {

    public OperationTest(String type) {
        super(type);
    }
    
    @Parameters
    public static List<Object[]> genParams() {
        return getTxnParams(null, true);
    }
}
