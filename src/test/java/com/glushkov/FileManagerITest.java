package com.glushkov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class FileManagerITest {
    FileManager fileManager = new FileManager();
    byte[] bytes;

    @Before
    public void setUp() throws Exception {
        new File("testDirectory\\dir1\\dir1_1").mkdirs();
        new File("testDirectory\\dir1\\file1.txt").createNewFile();
        new File("testDirectory\\dir1\\dir1_1\\file1_1.txt").createNewFile();
        new File("testDirectory\\dir1\\dir1_2").mkdirs();
        new File("testDirectory\\dir1\\dir1_2\\file1_2.txt").createNewFile();
        new File("testDirectory\\dir2").mkdirs();
        new File("testDirectory\\dir2\\file2.txt").createNewFile();
        new File("testDirectory\\dir3").mkdirs();
        new File("testDirectory\\dir3\\file3.txt").createNewFile();

        String testString = "Hello Universe";
        bytes = testString.getBytes();
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             OutputStream outputStream =
                     new FileOutputStream(new File("testDirectory\\dir1\\file1.txt"))) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    @Test
    public void countAllFiles() {
        int actual = fileManager.countFiles("testDirectory");
        assertEquals(5, actual);
    }

    @Test
    public void countOneFile() {
        int actual = fileManager.countFiles("testDirectory\\dir2");
        assertEquals(1, actual);
    }

    @Test
    public void countAllDirs() throws IOException {
        int actual = fileManager.countDirs("testDirectory");
        assertEquals(5, actual);
    }

    @Test
    public void countNoDirs() throws IOException {
        int actual = fileManager.countDirs("testDirectory\\dir2");
        assertEquals(0, actual);
    }

    @Test
    public void testCopyOneFile() throws IOException {
        fileManager.copy("testDirectory\\dir2\\file2.txt",
                "testDirectory\\dir3");

        int actual = fileManager.countFiles("testDirectory\\dir3");

        assertEquals(2, actual);
        assertTrue(new File("testDirectory\\dir2\\file2.txt").exists());
    }

    @Test
    public void testCopyHierarchyDirectoryAndFolders() throws IOException {
        fileManager.copy(new File("testDirectory\\dir2").getAbsolutePath(),
                new File("testDirectory\\dir3").getAbsolutePath());

        int actual = fileManager.countFiles("testDirectory\\dir3");
        assertEquals(2, actual);

        actual = fileManager.countDirs("testDirectory\\dir3");
        assertEquals(1, actual);
    }

    @Test
    public void testCopyCheckNames() throws IOException {
        fileManager.copy("testDirectory\\dir1\\dir1_1",
                "testDirectory\\dir2");

        assertTrue(new File("testDirectory\\dir2\\dir1_1").exists());
        assertTrue(new File("testDirectory\\dir2\\dir1_1\\file1_1.txt").exists());
    }

    @Test
    public void testCopyFileContentCheck() throws IOException {
        fileManager.copy("testDirectory\\dir1\\file1.txt", "testDirectory\\dir2");

        assertTrue(new File("testDirectory\\dir2\\file1.txt").exists());

        File fileTo = new File("testDirectory\\dir2\\file1.txt");
        byte[] bytesTo = new byte[(int) fileTo.length()];
        try (InputStream inputStreamTo = new FileInputStream(fileTo)) {
            inputStreamTo.read(bytesTo);

            for (int i = 0; i < bytesTo.length; i++) {
                assertEquals(bytes[i], bytesTo[i]);
            }
        }
    }

    @Test
    public void testMoveOneFile() throws IOException {
        fileManager.move("testDirectory\\dir2\\file2.txt", "testDirectory\\dir3");

        int actual = fileManager.countFiles("testDirectory\\dir2");
        assertEquals(0, actual);

        assertTrue(new File("testDirectory\\dir3\\file2.txt").exists());
        assertFalse(new File("testDirectory\\dir2\\file2.txt").exists());
    }

    @Test
    public void testMoveOneDirectory() throws IOException {
        fileManager.move("testDirectory\\dir2", "testDirectory\\dir3");

        int actual = fileManager.countDirs("testDirectory\\dir3");
        assertEquals(1, actual);

        actual = fileManager.countFiles("testDirectory\\dir3");
        assertEquals(2, actual);

        assertTrue(new File("testDirectory\\dir3\\dir2\\file2.txt").exists());
        assertFalse(new File("testDirectory\\dir2").exists());
    }

    @Test
    public void testMoveHierarchyDirectoryAndFolders() throws IOException {
        fileManager.move("testDirectory\\dir1", "testDirectory\\dir3");

        int actual = fileManager.countFiles("testDirectory\\dir3");
        assertEquals(4, actual);

        actual = fileManager.countDirs("testDirectory\\dir3");
        assertEquals(3, actual);

        assertTrue(new File("testDirectory\\dir3\\dir1\\dir1_1\\file1_1.txt").exists());
        assertFalse(new File("testDirectory\\dir1").exists());
    }

    @Test
    public void testMoveCheckNames() throws IOException {
        fileManager.move("testDirectory\\dir1", "testDirectory\\dir3");

        assertTrue(new File("testDirectory\\dir3\\dir1\\dir1_1\\file1_1.txt").exists());
        assertTrue(new File("testDirectory\\dir3\\dir1\\dir1_2\\file1_2.txt").exists());
    }

    @Test
    public void testMoveFileContentCheck() throws IOException {
        fileManager.move("testDirectory\\dir1\\file1.txt", "testDirectory\\dir2");

        assertFalse(new File("testDirectory\\dir1\\file1.txt").exists());
        assertTrue(new File("testDirectory\\dir2\\file1.txt").exists());

        File fileTo = new File("testDirectory\\dir2\\file1.txt");
        byte[] bytesTo = new byte[(int) fileTo.length()];
        try (InputStream inputStreamTo = new FileInputStream(fileTo)) {
            inputStreamTo.read(bytesTo);

            for (int i = 0; i < bytesTo.length; i++) {
                assertEquals(bytes[i], bytesTo[i]);
            }
        }
    }

    @Test
    public void testDelete() {
        fileManager.delete("testDirectory\\dir1\\dir1_1");
        assertFalse(new File("testDirectory\\dir1\\dir1_1").exists());
    }

    @Test
    public void testCopyService() throws IOException {
        fileManager.copyService(new File("testDirectory\\dir1\\file1.txt"),
                new File("testDirectory\\dir2\\file2.txt"));

        File fileTo = new File("testDirectory\\dir2\\file2.txt");
        byte[] bytesTo = new byte[(int) fileTo.length()];
        try (InputStream inputStreamTo = new FileInputStream(fileTo)) {
            inputStreamTo.read(bytesTo);

            for (int i = 0; i < bytesTo.length; i++) {
                assertEquals(bytes[i], bytesTo[i]);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationPath() {
        fileManager.validationPath("src\\dir");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationPathToDirectoryIsExist() {
        fileManager.validationPath("src\\dir");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationPathToDirectoryIsDirectory() {
        fileManager.validationPathToDirectory("testDirectory\\dir1\\file1.txt");
    }

    @After
    public void after() throws Exception {
        FileManager fileManager = new FileManager();
        fileManager.delete("testDirectory");
    }
}
