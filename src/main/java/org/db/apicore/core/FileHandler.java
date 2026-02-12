package org.db.apicore.core;

import java.io.IOException;
import java.nio.file.*;
import java.net.URL;
import java.util.Objects;

public class FileHandler {

    /**
     * Reads a file from a relative path and returns its contents as a string.
     */
    public static String readFile(String rootPath, String folderName, String fileName) {
        try {
            Path filePath = Paths.get(rootPath, folderName, fileName).toAbsolutePath();
            return Files.readString(filePath);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Error reading file from folder: %s with filename: %s", folderName, fileName), e);
        }
    }

    /**
     * Downloads a file from a URL into a target folder.
     */
    public static String downloadFile(String url, String targetFolder, String fileName) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(targetFolder);
        Objects.requireNonNull(fileName);

        Path folderPath = Paths.get(targetFolder);
        Path fullPath = folderPath.resolve(fileName);

        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            try (var inputStream = new URL(url).openStream()) {
                Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Unable to download file from URL: %s to %s. Error: %s",
                            url, fullPath, e.getMessage()), e);
        }
    }

    /**
     * Checks if a file exists in a folder.
     */
    public static boolean fileExists(String targetFolder, String fileName) {
        Path fullPath = Paths.get(targetFolder, fileName);
        return Files.exists(fullPath);
    }

    /**
     * Saves content to a file.
     */
    public static void saveFile(String rootPath, String folderName, String fileName, String contents) {
        try {
            Path filePath = Paths.get(rootPath, folderName, fileName).toAbsolutePath();
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, contents);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Error saving file to folder: %s with filename: %s", folderName, fileName), e);
        }
    }

    /**
     * Deletes a file if it exists.
     */
    public static void deleteFile(String targetFolder, String fileName) {
        Path fullPath = Paths.get(targetFolder, fileName);

        try {
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Unable to delete file: %s", fullPath), e);
        }
    }
}
