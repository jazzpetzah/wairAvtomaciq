package com.wire.picklejar.testclasses;

import cucumber.api.java.en.When;

public class TestStepClass {
    
    @When("^(.*) start(?:s|ing) something (.*)$")
    public void testStep(String arg1, String arg2) throws Exception {
        System.out.println(arg1+arg2);
    }
}
