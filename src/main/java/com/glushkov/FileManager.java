package com.glushkov;


import java.io.*;



public class FileManager {

    public int countFiles(String path) {
        validationPathToDirectory(path);
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

    void delete(String file) {
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

    void copyService(File fromFile, File toFile) {
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

    void validationPath(String path) {
        if (!new File(new File(path).getAbsolutePath()).exists()) {
            throw new IllegalArgumentException("Incorrect path " + path + ". Specified Path to File/Directory doesn`t exist . Please choose the correct path to the File/Directory and try again");
        }
    }

    void validationPathToDirectory(String path) {
        if (!new File(new File(path).getAbsolutePath()).exists()) {
            throw new IllegalArgumentException("Incorrect path " + path + ". Specified Path to Directory doesn`t exist. Please choose the correct path to the Directory and try again");
        }
        if (!new File(new File(path).getAbsolutePath()).isDirectory()) {
            throw new IllegalArgumentException("Incorrect path " + path + ". Specified Path isn`t a Directory. Please choose the correct path to the Directory and try again");
        }
    }
}
