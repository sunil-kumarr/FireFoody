package com.firefoody.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotificationModel {

    private String note_type;
    @ServerTimestamp
    Date timestamp;
    private String title;
    private String description;
    private String status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote_type() {
        return note_type;
    }

    public void setNote_type(String pNote_type) {
        note_type = pNote_type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date pTimestamp) {
        timestamp = pTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }
}
