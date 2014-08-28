/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 */
package com.sleepycat.je.rep.txn;

import java.util.Set;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * Test data for unit tests in this package.
 *
 * TestData has an id key, populated by a sequence, and a random value data
 * field. Testdata subclasses know whether they are meant to survive the syncup
 * or not, and put themselves in the appropriate test confirmation set. These
 * classes are static because the DPL does not support persistent inner
 * classes.  
 */
class Utils {

    @Entity
    abstract static class TestData {
        @PrimaryKey(sequence="ID")
        private long id;
        private int payload;

        TestData(int payload) {
            this.payload = payload;
        }
        
        TestData() {} // for deserialization

        @Override
        public String toString() {
            return "id=" + id + " payload=" + payload +
                " rollback=" + getRollback();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof TestData) {
                TestData t = (TestData) other;
                return ((t.id == id) && (t.payload==payload));
            }

            return false;
        }

        @Override
        public int hashCode() {
            return (int)(id + payload);
        }

        abstract boolean getRollback();
    }

    /* SavedData was committed, and should persist past rollbacks. */
    @Persistent
    static class SavedData extends TestData {

        SavedData(int payload, Set<TestData> saved) {
            super(payload);
            saved.add(this);
        }
        
        @SuppressWarnings("unused")
        private SavedData() {super();} // for deserialization

        boolean getRollback() {
            return false;
        }
    }

    /* RollbackData was uncommitted, and should disappear after rollbacks. */
    @Persistent
    static class RollbackData extends TestData {

        RollbackData(int payload, Set<TestData> rolledBack) {
            super(payload);
            rolledBack.add(this);
        }
        
        @SuppressWarnings("unused")
        private RollbackData() {super();} // for deserialization

        boolean getRollback() {
            return true;
        }     
    }
}
