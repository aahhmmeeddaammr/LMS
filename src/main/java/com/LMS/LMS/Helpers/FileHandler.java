package com.LMS.LMS.Helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    public static String SavePath = System.getProperty("user.dir") + File.separator + "uploads";

    static {
        File directory = new File(SavePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    public static void UploadFile(String fileName, byte[] fileData) throws IOException {
        Path filePath = Paths.get(SavePath, fileName);
        File file = filePath.toFile();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);
            System.out.println("File uploaded successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            throw e;
        }
    }
}
