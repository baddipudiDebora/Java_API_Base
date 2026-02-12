package org.db.apicore.tests;

import org.db.apicore.core.FileHandler;
import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTests {

    private static final String ROOT = "src/test/resources";
    private static final String TEST_FOLDER = "temp-test-folder";
    private static final String TEST_FILE = "testfile.txt";

    @BeforeEach
    void setup() throws Exception {
        Files.createDirectories(Paths.get(ROOT, TEST_FOLDER));
    }

    @AfterEach
    void cleanup() throws Exception {
        Path folder = Paths.get(ROOT, TEST_FOLDER);
        if (Files.exists(folder)) {
            Files.walk(folder)
                    .sorted((a, b) -> b.compareTo(a)) // delete children first
                    .forEach(path -> {
                        try { Files.delete(path); } catch (Exception ignored) {}
                    });
        }
    }

    @Test
    void testSaveFileAndReadFile() {
        String content = "Hello Framework!";
        FileHandler.saveFile(ROOT, TEST_FOLDER, TEST_FILE, content);

        String readContent = FileHandler.readFile(ROOT, TEST_FOLDER, TEST_FILE);

        assertEquals(content, readContent);
    }

    @Test
    void testFileExists() {
        FileHandler.saveFile(ROOT, TEST_FOLDER, TEST_FILE, "data");

        boolean exists = FileHandler.fileExists(
                Paths.get(ROOT, TEST_FOLDER).toString(),
                TEST_FILE
        );

        assertTrue(exists);
    }

    @Test
    void testDeleteFile() {
        FileHandler.saveFile(ROOT, TEST_FOLDER, TEST_FILE, "data");

        FileHandler.deleteFile(
                Paths.get(ROOT, TEST_FOLDER).toString(),
                TEST_FILE
        );

        boolean exists = FileHandler.fileExists(
                Paths.get(ROOT, TEST_FOLDER).toString(),
                TEST_FILE
        );

        assertFalse(exists);
    }

    @Test
    void testDownloadFile() {
        String url = "https://raw.githubusercontent.com/github/gitignore/main/Java.gitignore";
        String downloaded = FileHandler.downloadFile(
                url,
                Paths.get(ROOT, TEST_FOLDER).toString(),
                "downloaded.txt"
        );

        assertEquals("downloaded.txt", downloaded);

        boolean exists = FileHandler.fileExists(
                Paths.get(ROOT, TEST_FOLDER).toString(),
                "downloaded.txt"
        );

        assertTrue(exists);
    }
}
