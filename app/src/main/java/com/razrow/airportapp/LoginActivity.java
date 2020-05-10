package com.razrow.airportapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mFlightNumber;
    private Button Login;

    private User mUser;
    private Flight mFlight;
    private Flight purchasingFlight;
    private Purchase mPurchase;
    private Event mEvent;

    private UserDAO mUserDAO;

    private String mPasswordString;
    private String mUsernameString;
    private String mFlightNumberString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getDatabase();
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mFlightNumber = findViewById(R.id.flightNumber);

        Login = findViewById(R.id.buttonLoginAdd);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (checkForUserInDatabase()){
                    if (validatePassword()) {
                        if(checkForFlightInDatabase()){
                            mFlight = mUserDAO.getFlightByFlightNum(mFlightNumberString);
                            alertMaker();
                        }else{

                        }
                    } else {
                        openMainActivity();
                        toastMaker("Incorrect Password.");
                    }
                }else{
                    toastMaker("User could not be found, create a new account?");
                    openMainActivity();
                }
            }
        });
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private boolean checkForUserInDatabase(){
        mUser = mUserDAO.getUserByUsername(mUsernameString);
        if(mUser == null){
            Toast.makeText(this,"no user " + mUsernameString + " found",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkForFlightInDatabase(){
        mFlight = mUserDAO.getFlightByFlightNum(mFlightNumberString);
        if(mFlight == null){
            toastMaker(mFlightNumberString + " was not found.\n Check again.");
            openReserveActivity();
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPasswordString);
    }

    public void openReserveActivity(){
        Intent intent = new Intent(this, ReserveActivity.class);
        startActivity(intent);
    }

    private void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void toastMaker(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void getValuesFromDisplay(){
        mUsernameString = mUsername.getText().toString();
        mPasswordString = mPassword.getText().toString();
        mFlightNumberString = mFlightNumber.getText().toString();
    }

    private void alertMaker() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
        Bundle extras = getIntent().getExtras();
        int numTickets =  extras.getInt("tickets");

        alertBuilder.setMessage("CONFIRM PURCHASE?\nFlight Number: "+mFlight.getFlightNum()+ "\nDeparture: "
                + mFlight.getDeparture()+ "\nArrival: " + mFlight.getArrival()+ "\nDeparture Time: "
                + mFlight.getDepartureTime()+ "\nTickets: " + numTickets + "\nCost: " +
                mFlight.getPrice()*numTickets);

        alertBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle extras = getIntent().getExtras();
                        int numTickets = extras.getInt("tickets");
                        purchasingFlight = mUserDAO.getFlightByFlightNum(mFlightNumberString);
                        purchasingFlight.setTickets(numTickets);
                        if(mFlight.getTickets() < purchasingFlight.getTickets()){
                            openReserveActivity();
                            toastMaker("Not enough tickets.");
                        }
                        double cost = mFlight.getPrice()*numTickets;
                        mFlight.setTickets(mFlight.getTickets()-purchasingFlight.getTickets());
                        mUser = mUserDAO.getUserByUsername(mUsernameString);
                        mPurchase = new Purchase(purchasingFlight.getFlightNum(),
                                purchasingFlight.getDeparture(), purchasingFlight.getArrival(),
                                purchasingFlight.getDepartureTime(), purchasingFlight.getTickets(),
                                cost, mUser.getUserId());
                        mEvent = new Event("Reservation\n" + mUser.getUsername() + mPurchase.toString());
                        try{mUserDAO.insert(mPurchase);
                            mUserDAO.insert(mEvent);
                            mUserDAO.delete(mUserDAO.getFlightByFlightNum(mFlightNumberString));
                            mUserDAO.insert(mFlight);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        openMainActivity();
                        toastMaker("Flight Was Puchased");
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
}
