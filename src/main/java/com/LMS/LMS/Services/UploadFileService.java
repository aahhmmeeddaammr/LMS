package com.LMS.LMS.Services;

import com.LMS.LMS.Helpers.FileHandler;
import org.apache.poi.hpsf.GUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class UploadFileService {
    private static final String BASE_URL = "http://localhost:8080";

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            String fileName = new GUID()+"_"+file.getOriginalFilename();
            FileHandler.UploadFile(fileName, file.getBytes());
            String fileUrl = BASE_URL + "/uploads/" + fileName.replace(" ", "%20");
            return fileUrl;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}