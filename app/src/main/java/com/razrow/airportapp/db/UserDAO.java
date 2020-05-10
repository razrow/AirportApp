package com.razrow.airportapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Query;

import com.razrow.airportapp.Event;
import com.razrow.airportapp.Purchase;
import com.razrow.airportapp.User;
import com.razrow.airportapp.Flight;
import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUsername = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Flight flight);

    @Update
    void update(Flight... flights);

    @Delete
    void delete(Flight flight);

    @Query("SELECT * FROM " + AppDatabase.FLIGHT_TABLE + " WHERE mFlightNum = :flightNum" )
    Flight getFlightByFlightNum(String flightNum);

    @Query("SELECT * FROM " + AppDatabase.FLIGHT_TABLE)
    List<Flight> getAllFlights();

    @Query("SELECT * FROM " + AppDatabase.FLIGHT_TABLE + " WHERE mDeparture = :departure " + " AND mArrival = :arrival" )
    List<Flight> getFlightsByDepartureArrival(String departure, String arrival);

    @Query("SELECT * FROM " + AppDatabase.FLIGHT_TABLE + " WHERE mDeparture = :departure " + " AND mArrival = :arrival" )
    Flight getFlightByDepartureArrival(String departure, String arrival);

    @Insert
    void insert(Event... events);

    @Update
    void update(Event... events);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM " + AppDatabase.EVENT_TABLE + " Order BY mDate DESC")
    List<Event> getAllEvents();

    @Insert
    void insert(Purchase... purchases);

    @Update
    void update(Purchase... purchases);

    @Delete
    void delete(Purchase purchase);

    @Query("SELECT * FROM " + AppDatabase.PURCHASE_TABLE + " Order BY mDate DESC")
    List<Purchase> getAllPurchases();

    @Query("SELECT * FROM " + AppDatabase.PURCHASE_TABLE + " WHERE mUserId = :userId Order BY mDate DESC" )
    List<Purchase> getAllPurchasesByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.PURCHASE_TABLE + " WHERE mPurchaseId = :purchaseId")
    Purchase getByPurchaseId(int purchaseId);
}
