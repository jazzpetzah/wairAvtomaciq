package com.wire.picklejar;

import com.wire.picklejar.execution.PickleExecutor;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PickleJarJUnitProvider implements PickleJar {

    private static final Logger LOG = LogManager.getLogger();

    private PickleExecutor executor;
    private List<String> featureFiles = new ArrayList<>();

    public PickleJarJUnitProvider() {
        this.executor = new PickleExecutor();
    }
    
    @Override
    public PickleExecutor getExecutor() {
        return executor;
    }

    @Override
    public void reset() {
        this.executor = new PickleExecutor();
    }
    

}
