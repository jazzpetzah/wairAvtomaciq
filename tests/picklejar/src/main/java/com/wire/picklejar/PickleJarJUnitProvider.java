package com.wire.picklejar;

import com.wire.picklejar.execution.PickleExecutor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleJarJUnitProvider implements PickleJar {

    private static final Logger LOG = LoggerFactory.getLogger(PickleJarJUnitProvider.class);

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
