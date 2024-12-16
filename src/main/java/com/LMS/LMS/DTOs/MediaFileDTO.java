package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.MediaFile;

public class MediaFileDTO {
    public int id;
    public String Path;

    MediaFileDTO(MediaFile file){
        this.id = file.getId();
        this.Path = file.FilePath;
    }
}
