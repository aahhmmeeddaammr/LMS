package com.LMS.LMS.DTOs;

import com.LMS.LMS.Models.Lesson;

import java.util.Date;

public class LessonDTO {
    public int id;

    public String title;

    public String content;

    public Date date;

    LessonDTO(Lesson lesson) {
        this.id = lesson.id;
        this.title = lesson.title;
        this.content = lesson.content;
        this.date = lesson.date;
    }
}
