package com.razrow.airportapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.razrow.airportapp.db.AppDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUsername;
    private String mPassword;
    private boolean mIsAdmin;
    private Date mDate;

    public User(String username, String password, boolean isAdmin){
        mUsername = username;
        mPassword = password;
        mIsAdmin = isAdmin;
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }

    @Override
    public String toString() {
        return "Username: " + mUsername + "\nPassword: " +mPassword;
    }
}
