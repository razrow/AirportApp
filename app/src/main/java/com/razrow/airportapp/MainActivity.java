package com.razrow.airportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button mCreateAccount;
    Button mReserveSeat;
    Button mCancelReservation;
    Button mManageSystem;

    private UserDAO mUserDAO;

    private int mEventId = -1;

    private int mFlightId = -1;

    private SharedPreferences mPreferences = null;

    private static final String EVENT_ID_KEY = "com.razrow.airportapp.eventIdKey";
    private static final String FLIGHT_ID_KEY = "com.razrow.airportapp.flightIdKey";
    private static final String PREFERENCES_KEY = "com.razrow.airportapp.PREFERENCES_KEY";

    private int mUserId = -1;

    private User mUser;

    private static final String USER_ID_KEY = "com.razrow.airportapp.userIdKey";

    boolean m1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        checkForFlight();
        checkForUser();

        mCreateAccount = findViewById(R.id.buttonCreate);
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountActivity();
            }
        });

        mReserveSeat = findViewById(R.id.buttonReserve);
        mReserveSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReserveActivity();
            }
        });

        mCancelReservation = findViewById(R.id.buttonCancel);
        mCancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginCancelActivity();
            }
        });

        mManageSystem = findViewById(R.id.buttonManage);
        mManageSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageActivity();
            }
        });
    }

    public void openAccountActivity(){
        Intent intent = new Intent(this,AccountActivity.class);
        startActivity(intent);
    }

    public void openReserveActivity(){
        Intent intent = new Intent(this, ReserveActivity.class);
        startActivity(intent);
    }

    public void openManageActivity(){
        Intent intent = new Intent(this,ManageActivity.class);
        startActivity(intent);
    }

    public void openLoginCancelActivity(){
        Intent intent = new Intent(this,LoginCancelActivity.class);
        startActivity(intent);
    }

    private void checkForFlight(){
        //do we have a user in the intent?
        mFlightId = getIntent().getIntExtra(FLIGHT_ID_KEY, -1);

        //do we have a user in the prefs?
        if(mFlightId != -1){
            return;
        }

        if(mPreferences == null) {
            getPrefs();
        }

        mFlightId = mPreferences.getInt(FLIGHT_ID_KEY, -1);

        if(mFlightId != -1){
            return;
        }

        //do we have any users at all?
        List<Flight> flights = mUserDAO.getAllFlights();
        if(flights.size() <= 0 ){
            Flight defaultFlight = new Flight("Otter201","Monterey","Seattle","11:00AM",
                    2, 200.50);
            Flight defaultFlight2 = new Flight("Otter102","Los Angeles","Monterey",
                    "1:00(PM)",30,150);
            Flight defaultFlight3 = new Flight("Otter205", "Monterey","Seattle",
                    "3:37AM",13,38);
            Flight defaultFlight4 = new Flight("Otter101", "Monterey", "Los Angeles",
                    "10:00AM", 40, 150);
            mUserDAO.insert(defaultFlight);
            mUserDAO.insert(defaultFlight2);
            mUserDAO.insert(defaultFlight3);
            mUserDAO.insert(defaultFlight4);
        }
    }

    public void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void checkForUser(){
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        //do we have a user in the prefs?
        if(mUserId != -1){
            return;
        }

        if(mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        //do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0 ){
            User defaultUser = new User("admin2","admin2", true);
            mUserDAO.insert(defaultUser);
        }
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

}
