package com.wire.picklejar;

import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.scan.PickleJarScanner;
import java.util.Collection;

public interface PickleJar {
    
    public static Collection<Object[]> getTestcases() {
        return PickleJarScanner.getTestcases();
    }

    public static Collection<Object[]> getTestcases(String[] filterTags) {
        return PickleJarScanner.getTestcases(filterTags);
    }

    PickleExecutor getExecutor();
    
    void reset();
    
}
