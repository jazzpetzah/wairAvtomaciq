package com.wearezeta.auto.common.driver.device_helpers;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOSRealDeviceHelpers {
    private static Logger log = ZetaLogger.getLog(IOSRealDeviceHelpers.class.getSimpleName());

    private static final int CMDLINE_TIMEOUT_SECONDS = 30;

    /**
     * This will return only UDID of the first connected iDevice
     *
     * @return udid string
     * @throws Exception
     */
    public static Optional<String> getUDID() throws Exception {
        for (String deviceName : new String[]{"iPhone", "iPad"}) {
            final String result = CommonUtils.executeOsXCommandWithOutput(new String[]{
                    "/bin/bash",
                    "-c",
                    "system_profiler SPUSBDataType | sed -n '/"
                            + deviceName
                            + "/,/Serial/p' | grep 'Serial Number:' | awk -F ': ' '{print $2}'"}).trim();
            if (result.length() > 0) {
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }

    private static final String IOS_DEPLOY = "/usr/local/bin/ios-deploy";

    private static void executeIOSDeploy(String errorMsg, String... args) throws Exception {
        if (!new File(IOS_DEPLOY).exists()) {
            throw new IllegalStateException(
                    String.format("ios-deploy tool is not installed at path %s. " +
                            "Execute `npm install -g ios-deploy` to install it", IOS_DEPLOY));
        }
        final List<String> resultArgs = new ArrayList<>();
        resultArgs.add(IOS_DEPLOY);
        Collections.addAll(resultArgs, args);
        final Process p = new ProcessBuilder(resultArgs).
                redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
        if (!p.waitFor(CMDLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            p.destroy();

            throw new IllegalStateException(errorMsg);
        }
    }

    public static void uninstallApp(final String udid, final String bundleId) throws Exception {
        executeIOSDeploy(
                String.format("ios-deploy` has failed to perform '%s' uninstall.\n" +
                        "Please try to reconnect the device manually.", bundleId),
                "--id", udid, "--uninstall_only", "--bundle_id", bundleId
        );
    }

    public static void installApp(String udid, boolean shouldPerformAppReset, File appRoot) throws Exception {
        final List<String> defaultArgs = new ArrayList<>();
        Collections.addAll(defaultArgs, "--id", udid, "--bundle", appRoot.getCanonicalPath());
        if (shouldPerformAppReset) {
            defaultArgs.add("--uninstall");
        }
        executeIOSDeploy(
                String.format("ios-deploy` has failed to perform '%s' install.\n" +
                        "Please try to reconnect the device manually.", appRoot.getName()),
                defaultArgs.toArray(new String[defaultArgs.size()])
        );
    }

    private static final String IDEVICEINFO = "/usr/local/bin/ideviceinfo";

    public static String getMAC() throws Exception {
        if (!new File(IDEVICEINFO).exists()) {
            throw new IllegalStateException(
                    String.format("ideviceinfo tool is not installed at path %s. " +
                            "Execute `brew install --HEAD libimobiledevice` to install it", IDEVICEINFO));
        }
        final Process process = new ProcessBuilder(IDEVICEINFO).redirectErrorStream(true).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final StringBuilder output = new StringBuilder();
        String line;
        final String regex = "WiFiAddress:\\s+([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+)";
        final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        while ((line = reader.readLine()) != null) {
            final Matcher m = p.matcher(line);
            if (m.find()) {
                return m.group(1);
            } else {
                output.append(line).append("\n");
            }
        }
        throw new IllegalStateException(String.format("Cannot parse iDevice's MAC address from ideviceinfo output\n%s",
                output.toString()));
    }

    private static final String FPING = "/usr/local/bin/fping";
    private static final String ARP = "/usr/sbin/arp";

    /**
     * This method requires fping tool to be installed on the machine where the test is riunning:
     * brew install fping
     */
    public static String getIP() throws Exception {
        final String nodeIP = CommonUtils.getLocalIP4Address();
        final InetAddress localHost = Inet4Address.getLocalHost();
        final NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        final short prefixLength = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        final String fullIP = String.format("%s/%s", nodeIP, prefixLength);
        if (!new File(FPING).exists()) {
            throw new IllegalStateException(
                    String.format("fping tool is not installed at path %s. Execute `brew install fping` to install it",
                            FPING));
        }
        if (!new ProcessBuilder(FPING, "-c", "1", "-g", fullIP).redirectErrorStream(true).start().
                waitFor(120, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Cannot fping the subnetwork within 120 seconds timeout");
        }
        final Process arp = new ProcessBuilder(ARP, "-a").redirectErrorStream(true).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(arp.getInputStream()));
        final String macRegex = "([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+)";
        final Pattern macPattern = Pattern.compile(macRegex, Pattern.CASE_INSENSITIVE);
        final String ipRegex = "([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)";
        final Pattern ipPattern = Pattern.compile(ipRegex);
        final String deviceMAC = CommonUtils.normalizeMACAddress(getMAC());
        final StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            final Matcher macMatcher = macPattern.matcher(line);
            if (macMatcher.find() && CommonUtils.normalizeMACAddress(macMatcher.group(1)).equals(deviceMAC)) {
                final Matcher ipMatcher = ipPattern.matcher(line);
                if (ipMatcher.find()) {
                    return ipMatcher.group(1);
                }
            }
            output.append(line).append("\n");
        }
        throw new IllegalStateException(String.format("Cannot find IP address of iDEvice with MAC address %s in\n%s",
                deviceMAC, output.toString()));
    }
}
