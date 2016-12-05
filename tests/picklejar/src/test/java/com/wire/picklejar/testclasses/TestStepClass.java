package com.wire.picklejar.testclasses;

import org.junit.Ignore;


public class TestStepClass {
    
    @Ignore("^(.*) start(?:s|ing) something (.*)$")
    public void testStep(String arg1, String arg2) throws Exception {
        System.out.println(arg1+arg2);
    }
}
