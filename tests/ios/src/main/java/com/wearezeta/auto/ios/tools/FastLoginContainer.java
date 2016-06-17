package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

public class FastLoginContainer {
    @FunctionalInterface
    public interface IFutureIOSDriverGetter {
        Future<ZetaIOSDriver> create(String ipaPath,
                                     Optional<Map<String, Object>> additionalCaps,
                                     int retriesCount) throws Exception;
    }

    public static final String TAG_NAME = "@fastLogin";
    public static final String CAPABILITY_NAME = "fastLogin";

    private FastLoginContainer() {
    }

    private static FastLoginContainer instance = null;

    public synchronized static FastLoginContainer getInstance() {
        if (instance == null) {
            instance = new FastLoginContainer();
        }
        return instance;
    }

    private boolean isFastLoginEnabled = false;
    private Optional<IFutureIOSDriverGetter> driverGetter = Optional.empty();
    private Object[] driverGetterParams;

    public boolean isEnabled() {
        return isFastLoginEnabled && driverGetter.isPresent();
    }

    @SuppressWarnings("unchecked")
    public Future<ZetaIOSDriver> executeDriverCreation(ClientUser userToLogIn) throws Exception {
        final IFutureIOSDriverGetter driverToBeCreated = this.driverGetter.orElseThrow(() ->
                new IllegalStateException("Please enable fast login prior to call this method"));
        final Optional<Map<String, Object>> initialCapabilites =
                (Optional<Map<String, Object>>) this.driverGetterParams[1];
        final Map<String, Object> capabilites = new HashMap<>();
        if (initialCapabilites.isPresent()) {
            for (Map.Entry<String, Object> entry : initialCapabilites.get().entrySet()) {
                capabilites.put(entry.getKey(), entry.getValue());
            }
        }
        capabilites.put(CAPABILITY_NAME, userToLogIn);
        return driverToBeCreated.create((String) this.driverGetterParams[0], Optional.of(capabilites),
                (Integer) this.driverGetterParams[2]);
    }

    public void enable(IFutureIOSDriverGetter driverGetter, Object... driverGetterParams) {
        this.isFastLoginEnabled = true;
        this.driverGetter = Optional.of(driverGetter);
        assert driverGetterParams.length == 3;
        this.driverGetterParams = driverGetterParams;
    }

    public void reset() {
        isFastLoginEnabled = false;
        driverGetter = Optional.empty();
        driverGetterParams = null;
    }
}
