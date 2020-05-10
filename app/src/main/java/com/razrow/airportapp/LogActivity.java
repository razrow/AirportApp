package com.razrow.airportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

import java.util.List;

public class LogActivity extends AppCompatActivity {

    private Button mButton;
    private TextView mEventDisplay;

    private UserDAO mUserDAO;

    private int mFlightId = -1;

    private int mEventId;

    private Event mEvent;
    private List<Event> mEvents;

    private SharedPreferences mPreferences = null;

    private static final String EVENT_ID_KEY = "com.razrow.airportapp.eventIdKey";
    private static final String FLIGHT_ID_KEY = "com.razrow.airportapp.flightIdKey";
    private static final String PREFERENCES_KEY = "com.razrow.airportapp.PREFERENCES_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mEventDisplay = findViewById(R.id.eventDisplay);
        mButton = findViewById(R.id.buttonContinue);
        getDatabase();
        refreshDisplay();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlightActivity();
            }
        });
        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openMainActivity();
                return false;
            }
        });
    }


    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void refreshDisplay(){
        mEvents = mUserDAO.getAllEvents();

        if(mEvents.size() <= 0){
            mEventDisplay.setText("Ready to Search!");
        }

        StringBuilder sb = new StringBuilder();
        for(Event event : mEvents){
            sb.append(event);
            sb.append("\n");
            sb.append("-----------");
            sb.append("\n");
        }

        mEventDisplay.setText(sb.toString());
    }

    private void openFlightActivity(){
        Intent intent = new Intent(this,FlightActivity.class);
        startActivity(intent);
    }

    private void openMainActivity(){
        Intent intent = new Intent(LogActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
