package com.glushkov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class FileManagerTest {
    FileManager fileManager = new FileManager();
    byte[] bytes;
    @Before
    public void setUp() throws Exception {
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\dir1_1").mkdirs();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\file1.txt").createNewFile();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\dir1_1\\file1_1.txt").createNewFile();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\dir1_2").mkdirs();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\dir1_2\\file1_2.txt").createNewFile();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2").mkdirs();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file2.txt").createNewFile();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3").mkdirs();
        new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\file3.txt").createNewFile();

        String testString = "Hello Universe";
        bytes = testString.getBytes();
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             OutputStream outputStream =
            new FileOutputStream("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\file1.txt");){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    @Test
    public void countAllFiles() {
        int expected = 5;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir");
        assertEquals(expected, actual);
    }

    @Test
    public void countOneFile() {
        int expected = 1;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");
        assertEquals(expected, actual);
    }

    @Test
    public void countAllDirs() throws IOException {
        int expected = 5;
        int actual = fileManager.countDirs("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir");
        assertEquals(expected, actual);
    }

    @Test
    public void countNoDirs() throws IOException {
        int expected = 0;
        int actual = fileManager.countDirs("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");
        assertEquals(expected, actual);
    }

    @Test
    public void testCopyOneFile() throws IOException {
        fileManager.copy("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file2.txt",
                "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        int expected = 2;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        assertEquals(expected, actual);
        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file2.txt").exists());
    }

    @Test
    public void testCopyHierarchyDirectoryAndFolders() throws IOException {
        fileManager.copy("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2",
                "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        int expected = 2;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected, actual);

        expected = 1;
        actual = fileManager.countDirs("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected, actual);
    }

    @Test
    public void testCopyCheckNames() throws IOException {
        fileManager.copy("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\dir1_1",
                "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");

        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\dir1_1").exists());
        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\dir1_1\\file1_1.txt").exists());
    }

    @Test
    public void testCopyFileContentCheck() throws IOException {
        fileManager.copy("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\file1.txt",
                "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");

        assertTrue(new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file1.txt").exists());

        File fileTo = new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file1.txt");
        byte[] bytesTo = new byte[(int)fileTo.length()];
        try (InputStream inputStreamTo = new FileInputStream(fileTo)) {
            inputStreamTo.read(bytesTo);

            for (int i = 0; i < bytesTo.length; i++) {
                assertEquals(bytes[i], bytesTo[i]);
            }
        }
    }

    @Test
    public void testMoveOneFile() throws IOException {
        fileManager.move("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file2.txt",
                    "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        int expected = 0;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");
        assertEquals(expected, actual);

        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\file2.txt").exists());
        assertFalse(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file2.txt").exists());
    }

    @Test
    public void testMoveOneDirectory() throws IOException {
        fileManager.move("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2",
                         "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        int expected = 1;
        int actual = fileManager.countDirs("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected,actual);

        expected = 2;
        actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected,actual);

        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\dir2\\file2.txt").exists());
        assertFalse(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2").exists());
    }

    @Test
    public void testMoveHierarchyDirectoryAndFolders() throws IOException {
        fileManager.move("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1",
                        "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        int expected = 4;
        int actual = fileManager.countFiles("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected,actual);

        expected = 3;
        actual = fileManager.countDirs("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");
        assertEquals(expected,actual);

        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\dir1\\dir1_1\\file1_1.txt").exists());
        assertFalse(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1").exists());
    }

    @Test
    public void testMoveCheckNames() throws IOException {
        fileManager.move("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1",
                         "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3");

        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\dir1\\dir1_1\\file1_1.txt").exists());
        assertTrue(new File ("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir3\\dir1\\dir1_2\\file1_2.txt").exists());
    }

    @Test
    public void testMoveFileContentCheck() throws IOException {
        fileManager.move("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\file1.txt",
                         "C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2");

        assertFalse(new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir1\\file1.txt").exists());
        assertTrue(new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file1.txt").exists());

        File fileTo = new File("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir\\dir2\\file1.txt");
        byte[] bytesTo = new byte[(int)fileTo.length()];
        try(InputStream inputStreamTo = new FileInputStream(fileTo)){
            inputStreamTo.read(bytesTo);

            for (int i = 0; i < bytesTo.length; i++) {
                assertEquals(bytes[i], bytesTo[i]);
            }
        }
    }

    @After
    public void after() throws Exception{
    FileManager fileManager = new FileManager();
    fileManager.delete("C:\\Users\\DzeN-BooK\\IdeaProjects\\IO section\\src\\dir");
    }
}