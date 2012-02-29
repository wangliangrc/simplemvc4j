package com.clark.ifk;

import java.util.HashMap;

import junit.framework.TestCase;

public class IFKCImplTestCase extends TestCase {
    private IFKCImpl ifk;

    @Override
    protected void setUp() throws Exception {
        if (ifk == null) {
            ifk = new IFKCImpl(null);
        }
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testInit() throws Exception {
        if (ifk != null) {
            ifk = new IFKCImpl(null);
        }

        assertTrue(ifk.executor == null);
        assertTrue(ifk.temp instanceof Object[]);
        Object[] temp = (Object[]) ifk.temp;
        assertTrue(temp.length == 2);
        assertTrue(temp[0] instanceof HashMap);
        assertTrue(temp[1] instanceof HashMap);
    }
}
