package com.razrow.airportapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.razrow.airportapp.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.FLIGHT_TABLE)
public class Flight {

    @PrimaryKey(autoGenerate = true)
    private int mFlightId;

    private String mFlightNum;
    private String mDeparture;
    private String mArrival;
    private int mTickets;
    private String mDepartureTime;
    private double mPrice;

    public Flight(String flightNum, String departure, String arrival, String departureTime,
                  int tickets, double price){
        mFlightNum = flightNum;
        mDeparture = departure;
        mArrival = arrival;
        mDepartureTime = departureTime;
        mTickets = tickets;
        mPrice = price;

    }

    public String getFlightNum() {
        return mFlightNum;
    }

    public void setFlightNum(String flightNum) {
        this.mFlightNum = flightNum;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.mDepartureTime = departureTime;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public int getFlightId() {
        return mFlightId;
    }

    public void setFlightId(int flightId) {
        mFlightId = flightId;
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

    @Override
    public String toString() {
        return "Flight Number: " + mFlightNum + '\n' +
                "Departure: " + mDeparture + '\n' +
                "Arrival: " + mArrival + '\n' +
                "Departure Time:" + mDepartureTime + '\n' +
                "Capacity: " + mTickets + '\n' +
                "Price per Ticket: " + mPrice;
    }
}
