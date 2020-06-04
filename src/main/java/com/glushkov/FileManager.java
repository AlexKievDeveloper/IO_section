package com.glushkov;


import java.io.*;


public class FileManager {

    public int countFiles(String path) {
        validationPath(path);
        File file = new File(path);
        File[] files = file.listFiles();

        int countFiles = 0;

        for (File listFile : files) {
            if (listFile.isFile()) {
                countFiles++;

            } else if (listFile.isDirectory()) {
                countFiles += countFiles(listFile.getPath());
            }
        }
        return countFiles;
    }


    public int countDirs(String path) {
        validationPathToDirectory(path);
        File file = new File(path);
        File[] files = file.listFiles();

        int countDirs = 0;

        for (File listFile : files) {
            if (listFile.isDirectory()) {
                countDirs++;
                countDirs += countDirs(listFile.getPath());
            }
        }
        return countDirs;
    }


    public void copy(String from, String to) throws IOException {
        validationPath(from);
        validationPathToDirectory(to);

        File fileFrom = new File(from);
        File fileTo = new File(to, fileFrom.getName());

        if (fileFrom.isFile()) {
            if (fileTo.createNewFile()) {
                copyService(fileFrom, fileTo);
            }
        }

        if (fileFrom.isDirectory()) {
            if (fileTo.mkdir()) {
                for (File value : fileFrom.listFiles()) {
                    copy(value.toString(), fileTo.toString());
                }
            }
        }
    }

    public void move(String from, String to) throws IOException {
        validationPath(from);
        validationPathToDirectory(to);
        copy(from, to);
        delete(from);
    }

    protected void delete(String file) {
        File fileForDeleting = new File(file);
        if (fileForDeleting.isFile() || fileForDeleting.isDirectory() && ((fileForDeleting.listFiles().length) == 0)) {
            fileForDeleting.delete();
        } else {
            for (File internalFile : fileForDeleting.listFiles()) {
                delete(internalFile.toString());
            }
            delete(fileForDeleting.toString());
        }
    }

    private void copyService(File fromFile, File toFile) {
        try (InputStream inputStream = new FileInputStream(fromFile);
             OutputStream outputStream = new FileOutputStream(toFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validationPath(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException("Incorrect path " + path + ". Please choose the correct path to the directory and try again");
        }
    }

    private static void validationPathToDirectory(String path) {
        if (!new File(path).exists() || (!new File(path).isDirectory())) {
            throw new IllegalArgumentException("Incorrect path " + path + ". Please choose the correct path to the directory and try again");
        }
    }
}
