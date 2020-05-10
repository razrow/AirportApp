package com.razrow.airportapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.razrow.airportapp.db.AppDatabase;

import java.util.Date;
import java.util.List;

@Entity(tableName = AppDatabase.EVENT_TABLE)
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int mEventId;

    private String mAction;
    private Date mDate;

    public Event(String action){
        mAction = action;
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getEventId() {
        return mEventId;
    }

    public void setEventId(int eventId) {
        mEventId = eventId;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    @Override
    public String toString() {
        return "Event\n" + mAction + '\n' +
                "Date: " + mDate;
    }
}
