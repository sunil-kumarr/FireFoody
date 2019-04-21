package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class TimeStamp {
    @ServerTimestamp
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }
}
