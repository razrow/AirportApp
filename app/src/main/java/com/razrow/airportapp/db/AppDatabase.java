package com.razrow.airportapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.razrow.airportapp.Event;
import com.razrow.airportapp.Flight;
import com.razrow.airportapp.Purchase;
import com.razrow.airportapp.User;
import com.razrow.airportapp.db.typeConverters.DateTypeConverter;

@Database(entities = {User.class, Flight.class, Event.class, Purchase.class}, version = 8)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase  {
    public static final String DB_NAME = "USER_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String FLIGHT_TABLE = "FLIGHT_TABLE";
    public static final String EVENT_TABLE= "EVENT_TABLE";
    public static final String PURCHASE_TABLE = "PURCHASE_TABLE";

    public abstract UserDAO getUserDAO();
}
