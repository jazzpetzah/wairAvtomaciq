package compress.png;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Date;

import javax.imageio.ImageIO;

public class PngCompressor {
    private static boolean verbose;

    public PngCompressor() {
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = (new FileInputStream(sourceFile)).getChannel();
            destination = (new FileOutputStream(destFile)).getChannel();
            destination.transferFrom(source, 0L, source.size());
        } finally {
            if (source != null) {
                source.close();
            }

            if (destination != null) {
                destination.close();
            }
        }
    }

    private static byte[] getCompressedImageAsByteArray(BufferedImage image, int inputLen) throws IOException {
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream(inputLen / 2);
        PngEncoder encoder = new PngEncoder();
        encoder.setCompressed(true);
        encoder.write(image, tempStream);
        tempStream.flush();
        tempStream.close();
        return tempStream.toByteArray();
    }

    private static void writeSizeMessage(int originalSize, int compressedSize) {
        String diffMessage;
        if (originalSize == compressedSize) {
            diffMessage = " (same size)";
        } else {
            float diff = 1.0F - (float) compressedSize / (float) originalSize;
            diff *= 100.0F;
            if (originalSize > compressedSize) {
                diffMessage = " (" + String.format("%.02f", new Object[]{Float.valueOf(diff)}) + "% smaller)";
            } else {
                diff = -diff;
                diffMessage = " (" + String.format("%.02f", new Object[]{Float.valueOf(diff)}) + "% larger)";
            }
        }
        System.out.println("Original size: " + originalSize);
        System.out.println("Compressed size: " + compressedSize + diffMessage);
    }

    public static void compress(File input, File output) throws IOException {
        BufferedImage image;
        try {
            image = ImageIO.read(input);
        } catch (IOException var6) {
            System.out.println(new Date(System.currentTimeMillis()) + " " +
                    new Time(System.currentTimeMillis()) +
                    " error: Failed to read image " + input.getAbsolutePath());
            throw new IOException(var6);
        }

        if (image == null) {
            throw new IOException(new Date(System.currentTimeMillis()) + " " +
                    new Time(System.currentTimeMillis()) +
                    "error: Failed to read image " + input.getAbsolutePath());
        } else {
            int inputLen = (int) input.length();
            byte[] compressedImage = getCompressedImageAsByteArray(image, inputLen);
            if (verbose) {
                System.out.println("Input: " + input.getAbsolutePath());
                System.out.println("Output: " + output.getAbsolutePath());
                writeSizeMessage(inputLen, compressedImage.length);
            }

            if (compressedImage.length < inputLen) {
                if (verbose) {
                    System.out.println("Writing compressed image");
                }

                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
                outputStream.write(compressedImage);
                outputStream.flush();
                outputStream.close();
            } else if (!input.equals(output)) {
                if (verbose) {
                    System.out.println("Writing original image (compressed image is not smaller)");
                }

                copyFile(input, output);
            } else if (verbose) {
                System.out.println("Compressed image is not smaller, not overwriting");
            }

            if (verbose) {
                System.out.println();
            }
        }
    }

    public static void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] originalImage = getByteArrayFromInputStream(inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(originalImage);

        BufferedImage image;
        try {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException var6) {
            System.out.println(new Date(System.currentTimeMillis()) + " " +
                    new Time(System.currentTimeMillis()) +
                    "error: Failed to read image");
            throw new IOException(var6);
        }

        if (image == null) {
            throw new IOException(new Date(System.currentTimeMillis()) + " " +
                    new Time(System.currentTimeMillis()) +
                    "error: Failed to read image");
        } else {
            byte[] compressedImage = getCompressedImageAsByteArray(image, originalImage.length);
            if (verbose) {
                writeSizeMessage(originalImage.length, compressedImage.length);
            }

            if (compressedImage.length < originalImage.length) {
                if (verbose) {
                    System.out.println("Returning compressed image");
                }

                outputStream.write(compressedImage);
            } else {
                if (verbose) {
                    System.out.println("Returning original image (compressed image is not smaller)");
                }

                outputStream.write(originalImage);
            }

            if (verbose) {
                System.out.println();
            }
        }
    }

    private static byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];

        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        buffer.close();
        return buffer.toByteArray();
    }

    public static void compressPngsInFolder(final String folderPath) throws Exception {
        Files.walk(Paths.get(folderPath)).forEach(filePath -> {
                    if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith(".png")) {
                        try {
                            PngCompressor.compress(filePath.toFile(), filePath.toFile());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            showCommandLineHelp();
        } else {
            System.out.println("Starting PNG compression...");
            final long startTime = System.currentTimeMillis();
            if (args.length > 0) {
                verbose = "true".equalsIgnoreCase(System.getProperty("verbose"));
                compressPngsInFolder(args[0]);
                System.out.println("PNG compression finished after " + (System.currentTimeMillis() - startTime) / 1000 + " " +
                        "seconds");
            } else System.out.println("<Path> to compress is missed");
        }
    }

    private static void showCommandLineHelp() {
        System.out.println("How to run PngCompressor:\n\n    java [options] -jar compress-png.jar <File/Folder Path>\n" +
                "Example: java -Dverbose=true -jar compress-png.jar <FolderPath>");
    }
}
