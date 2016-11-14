package com.wire.picklejar.scan;

import com.wire.picklejar.testclasses.TestStepClass;
import java.io.File;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JavaSeekerTest {

    @Test
    public void testGetClassesZero() throws Exception {
        Collection<Class> result = JavaSeeker.getClasses(new String[]{"com.wire.picklejar.nothing.there"});
        assertTrue("Classes collection is null", null != result);
        assertTrue("Classes collection size is NOT zero", result.isEmpty());
    }

    @Test
    public void testGetClasses() throws Exception {
        Collection<Class> classes = JavaSeeker.getClasses(new String[]{"com.wire.picklejar.testclasses"});
        assertTrue("Classes collection size is zero", !classes.isEmpty());
        assertEquals("Classname is wrong", 
                TestStepClass.class.getCanonicalName(), 
                classes.stream().findFirst().get().getCanonicalName());
    }

    @Test
    public void testGetResource() throws Exception {
        Collection<File> result = JavaSeeker.getResource("com.wire.picklejar.testresources", "feature");
        assertTrue("Found file does NOT exist", result.stream().findFirst().get().exists());
        assertEquals("File name is wrong", 
                "TestFeature.feature", 
                result.stream().findFirst().get().getName());
    }

    @Test
    public void testGetLoadedClasses() throws Exception {
        Collection<Class<?>> result = JavaSeeker.getLoadedClasses();
        assertTrue("Loaded classes collection size is zero", !result.isEmpty());
    }

}
