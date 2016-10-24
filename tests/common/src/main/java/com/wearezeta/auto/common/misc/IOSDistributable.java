package com.wearezeta.auto.common.misc;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IOSDistributable {
    private static Map<String, IOSDistributable> instances = new HashMap<>();

    public static IOSDistributable getInstance(String path) {
        if (!instances.containsKey(path)) {
            instances.put(path, new IOSDistributable(path));
        }
        return instances.get(path);
    }

    private File appPath;
    private boolean isAppRootTemporary = false;
    private Optional<String> bundleId = Optional.empty();

    private IOSDistributable(String path) {
        try {
            final File fPath = new File(path);
            if (path.toLowerCase().endsWith(".app")) {
                appPath = fPath;
            } else if (path.toLowerCase().endsWith(".ipa")) {
                appPath = CommonUtils.extractAppFromIpa(appPath);
                isAppRootTemporary = true;
            } else {
                throw new IllegalArgumentException(
                        String.format("Only .ipa and .app packages are supported. %s is given instead", path)
                );
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    public File getAppRoot() {
        return appPath;
    }

    private static String parseBundleId(File plist) throws Exception {
        final NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(plist);
        return rootDict.objectForKey("CFBundleIdentifier").toString();
    }

    public String getBundleId() throws Exception {
        if (!this.bundleId.isPresent()) {
            this.bundleId = Optional.of(parseBundleId(new File(appPath.getCanonicalPath() + "/Info.plist")));
        }
        return this.bundleId.get();
    }

    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    if (isAppRootTemporary) {
                        FileUtils.deleteDirectory(appPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
