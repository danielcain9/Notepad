package com.daniel.notepad.Model;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;


public class Note {
    private String title;
    private String body;
    private String date;


    public Note(String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu kk:mm");
        org.threeten.bp.LocalDateTime dateTime = org.threeten.bp.LocalDateTime.parse(date,dateTimeFormatter);

        return dateTime;
    }
}
