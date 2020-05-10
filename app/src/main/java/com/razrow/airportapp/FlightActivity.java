package com.razrow.airportapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

import java.util.List;

public class  FlightActivity extends AppCompatActivity {

    private EditText flightNumber;
    private EditText departLoc;
    private EditText arrivLoc;
    private EditText departTime;
    private EditText flightCap;
    private EditText cost;
//    private Button mButton;
    private Button mAdd;
//    private TextView mEventDisplay;

    private String flightNum;
    private String mDeparture;
    private String mArrival;
    private int mTickets;
    private String departureTime;
    private double price;
    private Flight mFlight;

    private Event mEvent;
    private List<Event> mEvents;

    private UserDAO mUserDAO;

    private int mFlightId = -1;

    private int mEventId;



    private SharedPreferences mPreferences = null;

    private static final String EVENT_ID_KEY = "com.razrow.airportapp.eventIdKey";
    private static final String FLIGHT_ID_KEY = "com.razrow.airportapp.flightIdKey";
    private static final String PREFERENCES_KEY = "com.razrow.airportapp.PREFERENCES_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        getDatabase();
        checkForFlight();
        setUp();
//        refreshDisplay();
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputLetters(departLoc.getText().toString()) &&
                validateInputLetters(arrivLoc.getText().toString()) &&
                validateInputTime(departTime.getText().toString()) &&
                validateInputTickets(flightCap.getText().toString()) &&
                validateInputPrice(cost.getText().toString())){
                    mFlight = createFlight();
                    if(!currentFlight(mFlight)){
                        toastMaker("Flight Number in Use");
                        openFlightActivity();
                    }
                    alertMaker();
                }
            }
        });
        mAdd.setOnLongClickListener(new View.OnLongClickListener() {
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

    private void alertMaker() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FlightActivity.this);

        Flight flight = createFlight();
        alertBuilder.setMessage("CONFIRM?\n"+flight.toString());

        alertBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Flight flight = createFlight();
                        Event event = createEvent(flight);
                        try{mUserDAO.insert(flight);
                            mUserDAO.insert(event);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        toastMaker("Flight Was Created\n" + flight.toString());
                        openMainActivity();
                    }
                });

        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openMainActivity();
                        toastMaker("Flight Was not Made");
                    }
                });

        alertBuilder.setCancelable(true);

        alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                toastMaker("Dialog Cancelled");
            }
        });

        alertBuilder.create().show();
    }

    private void openFlightActivity(){
        Intent intent = new Intent(this,FlightActivity.class);
        startActivity(intent);
    }

    private boolean validateInputTickets(String input){
        try{
            int value = Integer.parseInt(input);
            if(value < 1){
                toastMaker("Invalid Ticket Amount");
                return false;
            }
            return true;
        }catch(NumberFormatException e){
            toastMaker("Invalid Ticket Amount");
            return false;
        }
    }

    private boolean currentFlight(Flight flight){
        if(mUserDAO.getFlightByFlightNum(flight.getFlightNum()) == null)
            return true;
        else
            return false;
    }

    private boolean validateInputLetters(String input){
        char letterCount = 0;
        for(int i = 0; i<input.length(); i++){
            if((input.charAt(i) <= 90 && input.charAt(i) >= 65) || (input.charAt(i) == 32) ||
                    (input.charAt(i) <= 122 && input.charAt(i) >= 97)){
                letterCount++;
            }
        }
        if(letterCount == input.length()){
            return true;
        }else{
            toastMaker("Invalid Departure or Arrival Location");
            return false;
        }
    }

    private boolean validateInputTime(String input){
        char letterCount = 0;
        for(int i = 0; i<input.length(); i++){
            if((input.charAt(i) <= 58 && input.charAt(i) >= 48) ||
                    //AM or am
                    (input.charAt(i) == 65) || (input.charAt(i) == 97) ||
                    (input.charAt(i) == 77) || (input.charAt(i) == 109) ||
                    (input.charAt(i) == 80) || (input.charAt(i) == 112)){
                letterCount++;
            }
        }
        if(letterCount == input.length()){
            return true;
        }else{
            toastMaker("Invalid Departure Time");
            return false;
        }
    }

    private boolean validateInputPrice(String input){
        try{
            double value = Double.parseDouble(input);
            if(value <= 0){
                toastMaker("Invalid Price Amount");
                return false;
            }
            return true;
        }catch(NumberFormatException e){
            toastMaker("Invalid Price Amount");
            return false;
        }
    }

    private Event createEvent(Flight flight){
        Event event = new Event("Create A Flight\n" + flight.toString());
        return event;
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
            mUserDAO.insert(defaultFlight);
        }
        //TODO
//        openMainActivity();
    }

    public void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private Flight createFlight(){
        flightNum = flightNumber.getText().toString();
        mDeparture = departLoc.getText().toString();
        mArrival = arrivLoc.getText().toString();
        departureTime =  departTime.getText().toString();
        mTickets = Integer.parseInt(flightCap.getText().toString());
        price = Double.parseDouble(cost.getText().toString());

        Flight flight = new Flight(flightNum,mDeparture,mArrival,departureTime,
                mTickets,price);
        return flight;
    }

    private void toastMaker(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void setUp(){
        flightNumber = findViewById(R.id.flightNumber);
        departLoc = findViewById(R.id.departure);
        arrivLoc = findViewById(R.id.arrival);
        departTime = findViewById(R.id.departureTime);
        flightCap = findViewById(R.id.capacity);
        cost = findViewById(R.id.price);;
        mAdd = findViewById(R.id.buttonAdd);
    }

}
