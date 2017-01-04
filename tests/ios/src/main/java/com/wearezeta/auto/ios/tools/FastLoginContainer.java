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
        Future<ZetaIOSDriver> create(String appPath,
                                     Optional<Map<String, Object>> additionalCaps,
                                     int retriesCount) throws Exception;
    }

    public static final String CAPABILITY_NAME = "fastLogin";
    public static final String TAG_NAME = "@" + CAPABILITY_NAME;

    public FastLoginContainer() {
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
        final Optional<Map<String, Object>> initialCapabilities =
                (Optional<Map<String, Object>>) this.driverGetterParams[1];
        final Map<String, Object> capabilities = new HashMap<>();
        if (initialCapabilities.isPresent()) {
            for (Map.Entry<String, Object> entry : initialCapabilities.get().entrySet()) {
                capabilities.put(entry.getKey(), entry.getValue());
            }
        }
        capabilities.put(CAPABILITY_NAME, userToLogIn);
        return driverToBeCreated.create((String) this.driverGetterParams[0], Optional.of(capabilities),
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
