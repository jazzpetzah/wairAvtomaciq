package compress.png;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CompressPNG {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting PNG compression...");
        final long startTime = System.currentTimeMillis();
        if (args.length > 0) {
            compressPngsInFolder(args[0]);
            System.out.println("PNG compression finished after " + (System.currentTimeMillis() - startTime) / 1000 + " " +
                    "seconds");
        } else System.out.println("<Path> to compress is missed");
    }

    public static void compressPngsInFolder(final String folderPath) throws Exception {
        Files.walk(Paths.get(folderPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith(".png")) {
                try {
                    PngCompressor.compress(filePath.toFile(), filePath.toFile());
                    System.out.println("processing " + filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}