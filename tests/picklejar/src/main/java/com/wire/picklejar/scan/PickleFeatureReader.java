package com.wire.picklejar.scan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleFeatureReader {
    
    private static final Logger LOG = LoggerFactory.getLogger(PickleFeatureReader.class.getSimpleName());

    public static List<String> readFolders(File[] files) throws IOException {
        List<String> features = new ArrayList<>();
        for (File file : files) {
            features.addAll(readFolder(file));
        }
        return features;
    }

    public static List<String> readFolders(String[] paths) throws IOException {
        List<String> features = new ArrayList<>();
        for (String path : paths) {
            features.addAll(readFolder(path));
        }
        return features;
    }
    
    public static List<String> readFiles(List<File> files) throws IOException {
        List<String> features = new ArrayList<>();
        for (File file : files) {
            features.add(readFile(file));
        }
        return features;
    }
    
    public static List<String> readFiles(File[] files) throws IOException {
        return readFiles(Arrays.asList(files));
    }

    public static List<String> readFolder(String path) throws IOException {
        return readFolder(Paths.get(path).toFile());
    }

    public static List<String> readFolder(File folder) throws IOException {
        List<String> features = new ArrayList<>();
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                features.addAll(readFolders(file.listFiles()));
            }
        } else {
            features.add(readFile(folder));
        }
        return features;
    }

    public static String readFile(File file) throws IOException {
        LOG.info("Reading file: {}", file.getName());
        if (!file.isFile() || !file.getName().contains(".feature")) {
            throw new IllegalArgumentException("Provided file is a folder or does not have extension '.feature'");
        }
        final String feature = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        return feature;
    }
}
