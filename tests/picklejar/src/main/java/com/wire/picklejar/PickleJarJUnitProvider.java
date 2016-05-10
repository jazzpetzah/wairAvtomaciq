package com.wire.picklejar;

import com.wire.picklejar.execution.PickleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleJarJUnitProvider implements PickleJar {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(PickleJarJUnitProvider.class.getSimpleName());

    private PickleExecutor executor;

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
