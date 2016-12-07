package com.wire.picklejar.scan;

import com.wire.picklejar.testclasses.TestStepClass;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

public class PickleAnnotationSeekerTest {
    
    @Test
    public void testGetMethodsAnnotatedWith() {
        List<Method> result = PickleAnnotationSeeker.getMethodsAnnotatedWith(TestStepClass.class, Ignore.class);
        assertTrue("Method collection is empty", !result.isEmpty());
        assertEquals("Method name is wrong", 
                "testStep", 
                result.stream().findFirst().get().getName());
        assertEquals("Method parameter count is wrong", 
                2, 
                result.stream().findFirst().get().getParameterCount());
    }
    
    @Test
    public void testGetMethodsAnnotatedWithZero() {
        List<Method> result = PickleAnnotationSeeker.getMethodsAnnotatedWith(TestStepClass.class, Test.class);
        assertTrue("Method collection is null", null != result);
        assertTrue("Method collection is NOT empty", result.isEmpty());
    }
    
}
