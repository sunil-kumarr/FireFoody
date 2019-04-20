package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotificationModel {

    private String note_type;
    @ServerTimestamp
    Date timestamp;
    private String title;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean pStatus) {
        status = pStatus;
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
