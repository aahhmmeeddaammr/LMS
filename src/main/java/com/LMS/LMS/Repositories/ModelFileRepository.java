package com.LMS.LMS.Repositories;

import com.LMS.LMS.Models.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelFileRepository extends JpaRepository<MediaFile,Integer> {
}
