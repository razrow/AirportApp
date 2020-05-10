package com.razrow.airportapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.razrow.airportapp.Flight;
import com.razrow.airportapp.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.PURCHASE_TABLE)
public class Purchase {
    @PrimaryKey(autoGenerate = true)
    private int mPurchaseId;

    private String mFlightNum;
    private String mDeparture;
    private String mArrival;
    private int mTickets;
    private String mDepartureTime;
    private double mPrice;

    private int mUserId;
    private Date mDate;

    public Purchase(String flightNum, String departure, String arrival, String departureTime,
                    int tickets, double price, int userId){
        mFlightNum = flightNum;
        mDeparture = departure;
        mArrival = arrival;
        mDepartureTime = departureTime;
        mTickets = tickets;
        mPrice = price;
        mUserId = userId;

        mDate = new Date();
    }

    public int getPurchaseId() {
        return mPurchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        mPurchaseId = purchaseId;
    }

    public String getFlightNum() {
        return mFlightNum;
    }

    public void setFlightNum(String flightNum) {
        mFlightNum = flightNum;
    }

    public String getDeparture() {
        return mDeparture;
    }

    public void setDeparture(String departure) {
        mDeparture = departure;
    }

    public String getArrival() {
        return mArrival;
    }

    public void setArrival(String arrival) {
        mArrival = arrival;
    }

    public int getTickets() {
        return mTickets;
    }

    public void setTickets(int tickets) {
        mTickets = tickets;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        mDepartureTime = departureTime;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "Purchase Id: " + mPurchaseId + "\n" +
                "Flight Number: " + mFlightNum + '\n' +
                "Departure: " + mDeparture + '\n' +
                "Arrival: " + mArrival + '\n' +
                "Departure Time:" + mDepartureTime + '\n' +
                "Capacity: " + mTickets + '\n' +
                "Price per Ticket: " + mPrice;
    }
}
