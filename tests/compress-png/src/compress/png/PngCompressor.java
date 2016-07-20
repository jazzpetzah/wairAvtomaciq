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
import javax.imageio.ImageIO;

public class PngCompressor {
    private static final String FILENAME = "_compressed";
    private static boolean verbose;

    public PngCompressor() {
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = (new FileInputStream(sourceFile)).getChannel();
            destination = (new FileOutputStream(destFile)).getChannel();
            destination.transferFrom(source, 0L, source.size());
        } finally {
            if(source != null) {
                source.close();
            }

            if(destination != null) {
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
        if(originalSize == compressedSize) {
            diffMessage = " (same size)";
        } else {
            float diff = 1.0F - (float)compressedSize / (float)originalSize;
            diff *= 100.0F;
            if(originalSize > compressedSize) {
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
            System.out.println("error: Failed to read image " + input.getAbsolutePath());
            throw new IOException(var6);
        }

        if(image == null) {
            throw new IOException("error: Failed to read image " + input.getAbsolutePath());
        } else {
            int inputLen = (int)input.length();
            byte[] compressedImage = getCompressedImageAsByteArray(image, inputLen);
            if(verbose) {
                System.out.println("Input: " + input.getAbsolutePath());
                System.out.println("Output: " + output.getAbsolutePath());
                writeSizeMessage(inputLen, compressedImage.length);
            }

            if(compressedImage.length < inputLen) {
                if(verbose) {
                    System.out.println("Writing compressed image");
                }

                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
                outputStream.write(compressedImage);
                outputStream.flush();
                outputStream.close();
            } else if(!input.equals(output)) {
                if(verbose) {
                    System.out.println("Writing original image (compressed image is not smaller)");
                }

                copyFile(input, output);
            } else if(verbose) {
                System.out.println("Compressed image is not smaller, not overwriting");
            }

            if(verbose) {
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
            System.out.println("error: Failed to read image");
            throw new IOException(var6);
        }

        if(image == null) {
            throw new IOException("error: Failed to read image");
        } else {
            byte[] compressedImage = getCompressedImageAsByteArray(image, originalImage.length);
            if(verbose) {
                writeSizeMessage(originalImage.length, compressedImage.length);
            }

            if(compressedImage.length < originalImage.length) {
                if(verbose) {
                    System.out.println("Returning compressed image");
                }

                outputStream.write(compressedImage);
            } else {
                if(verbose) {
                    System.out.println("Returning original image (compressed image is not smaller)");
                }

                outputStream.write(originalImage);
            }

            if(verbose) {
                System.out.println();
            }

        }
    }

    private static byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];

        int nRead;
        while((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        buffer.close();
        return buffer.toByteArray();
    }

    private static boolean processFile(File inputFile, boolean overwrite) {
        String fileLocation = inputFile.getAbsolutePath();
        if(inputFile.exists()) {
            if(fileLocation.toLowerCase().endsWith(".png")) {
                File outputFile;
                if(overwrite) {
                    outputFile = inputFile;
                } else {
                    int e = fileLocation.length() - 4;
                    outputFile = new File(fileLocation.substring(0, e) + "_compressed" + fileLocation.substring(e));
                }

                try {
                    compress(inputFile, outputFile);
                    return true;
                } catch (IOException var5) {
                    var5.printStackTrace();
                    return false;
                }
            } else {
                System.out.println("error: " + fileLocation + " is not a png file.");
                return false;
            }
        } else {
            System.out.println("error: " + fileLocation + " does not exist.");
            return false;
        }
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            showCommandLineHelp();
        } else {
            boolean failed = false;
            verbose = "true".equalsIgnoreCase(System.getProperty("verbose"));
            boolean overwrite = "true".equalsIgnoreCase(System.getProperty("overwrite"));
            String[] var3 = args;
            int var4 = args.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String arg = var3[var5];
                boolean success = processFile(new File(arg), overwrite);
                if(!success) {
                    failed = true;
                }
            }

            if(failed) {
                System.exit(1);
            }
        }

    }

    private static void showCommandLineHelp() {
        System.out.println("How to run PngCompressor:\n\n    java [options] -jar jdeli.jar compresspng [pngfile [pngfile ...]]\n\n        options:\n            1. -Doverwrite=true       overwrite input files (default: \"false\")\n            2. -Dverbose=true         print status messages (default: \"false\")\n\n    Examples:\n        java -jar jdeli.jar compresspng file.png\n\n        java -Doverwrite=true -jar jdeli.jar compresspng /directory/*.png\n\n        java -Dverbose=true -jar jdeli.jar compresspng /directory/*.png\n\n    The output filename is the same as the input name except that _compressed\n    will be appended to the name. E.g. file.png will become file_compressed.png\n    This can be changed to overwrite the existing file by setting the overwrite\n    setting to true.");
    }
}
