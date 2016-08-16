package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class RealDeviceHelpers {
    private static Logger log = ZetaLogger.getLog(RealDeviceHelpers.class.getSimpleName());


    private static final int APP_UNINSTALL_TIMEOUT_SECONDS = 10;
    private static final int DEVICE_RECONNECT_TIMEOUT_SECONDS = 30;

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

    private static void waitUntilIsConnected(String udid, int timeoutSeconds) throws Exception {
        log.debug(String.format("Waiting %s seconds until the device is connected...",
                DEVICE_RECONNECT_TIMEOUT_SECONDS));
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000) {
            final Process p = new ProcessBuilder(
                    "/usr/local/bin/idevicediagnostics", "-u", udid, "diagnostics", "WiFi"
            ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
            final int retCode = p.waitFor();
            if (retCode == 0) {
                return;
            } else {
                Thread.sleep(2000);
            }
        }
        throw new IllegalStateException(String.format(
                "iOS device with UDID %s has NOT been successfully reconnected after %s seconds", udid, timeoutSeconds));
    }

    private static final String RECONNECT_DEVICE_SCRIPT_NAME = "reconnectIDevice.py";

    public static void uninstallApp(final String udid, final String bundleId) throws Exception {
        final Process p = new ProcessBuilder(
                "/usr/local/bin/ideviceinstaller", "-u", udid, "-U", bundleId
        ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
        if (!p.waitFor(APP_UNINSTALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            // FIXME: Workaround for https://github.com/appium/appium/issues/5039
            p.destroy();

            if (CommonUtils.isRunningInJenkinsNetwork()) {
                log.warn("Seems like ideviceinstaller has frozen. Trying to reconnect the IDevice to its VM...");
                new ProcessBuilder("/usr/bin/python",
                        getIOSToolsRoot(IOSSimulatorHelper.class) + File.separator + RECONNECT_DEVICE_SCRIPT_NAME
                ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor();
                waitUntilIsConnected(udid, DEVICE_RECONNECT_TIMEOUT_SECONDS);
                final Process p1 = new ProcessBuilder(
                        "/usr/local/bin/ideviceinstaller", "-u", udid, "-U", bundleId
                ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
                if (!p1.waitFor(APP_UNINSTALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("ideviceinstaller has failed to perform application uninstall.\n" +
                            "Please try to reconnect the device.");
                }
            } else {
                throw new IllegalStateException("ideviceinstaller has failed to perform application uninstall.\n" +
                        "Please try to reconnect the device.");
            }
        }
    }

    private static final String IDEVICEINFO = "/usr/local/bin/ideviceinfo";

    public static String getMAC() throws Exception {
        if (!new File(IDEVICEINFO).exists()) {
            throw new IllegalStateException(
                    String.format("ideviceinfo tool is not installed at path %s. " +
                            "Execute `brew install ideviceutils` to install it", IDEVICEINFO));
        }
        final Process arp = new ProcessBuilder(new String[]{IDEVICEINFO}).start();
        arp.waitFor();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(arp.getInputStream()));
        final StringBuilder output = new StringBuilder();
        String line;
        final String regex = "WiFiAddress:\\s+([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+)";
        final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        while ( (line = reader.readLine()) != null) {
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
     *
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
        new ProcessBuilder(new String[]{FPING, "-c", "1", "-g", fullIP}).start().waitFor();
        final Process arp = new ProcessBuilder(new String[]{ARP, "-a"}).start();
        arp.waitFor();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(arp.getInputStream()));
        final String macRegex = "([0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+)";
        final Pattern macPattern = Pattern.compile(macRegex, Pattern.CASE_INSENSITIVE);
        final String ipRegex = "([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)";
        final Pattern ipPattern = Pattern.compile(ipRegex);
        final String deviceMAC = CommonUtils.normalizeMACAddress(getMAC());
        final StringBuilder output = new StringBuilder();
        String line;
        while ( (line = reader.readLine()) != null) {
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
