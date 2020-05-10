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

public class CancellationActivity extends AppCompatActivity {

    private Button mButton;
    private TextView mPurchaseDisplay;
    private EditText mCancelId;

    private UserDAO mUserDAO;
    private Flight mFlight;
    private Event mEvent;

    private int mFlightId = -1;

    private int mPurchaseId;
    private int mUserId;
    private User mUser;

    private Purchase mPurchase;
    private List<Purchase> mPurchases;

    private SharedPreferences mPreferences = null;

    private static final String PURCHASE_ID_KEY = "com.razrow.airportapp.purchaseIdKey";
    private static final String USER_ID_KEY = "com.razrow.airportapp.userIdKey";
    private static final String PREFERENCES_KEY = "com.razrow.airportapp.PREFERENCES_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);
        getDatabase();
        mPurchaseDisplay = findViewById(R.id.purchaseDisplay);
        mButton = findViewById(R.id.buttonContinue);
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mCancelId = findViewById(R.id.purchaseNumber);
        refreshDisplay();
        if(mPurchases.isEmpty()){
            toastMaker("You have not made any purchases!");
            openMainActivity();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePurchaseId(mCancelId.getText().toString())){
                    mPurchase = mUserDAO.getByPurchaseId(Integer.parseInt(mCancelId.getText().toString()));
                    alertMaker();
                }
            }
        });
    }

    private void alertMaker() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CancellationActivity.this);
        alertBuilder.setMessage("Cancel?\n" + mPurchase);

        alertBuilder.setPositiveButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFlight = mUserDAO.getFlightByFlightNum(mPurchase.getFlightNum());
                        mUser = mUserDAO.getUserByUserId(mPurchase.getUserId());
                        mFlight.setTickets(mFlight.getTickets()+mPurchase.getTickets());
                        mEvent = new Event("Cancelled:\n" + mUser.getUsername() + "\n" + mPurchase.toString());
                        try {mUserDAO.insert(mEvent);
                            mUserDAO.delete(mUserDAO.getByPurchaseId(mPurchase.getPurchaseId()) );
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        openMainActivity();
                        toastMaker("Purchase was cancelled.");
                    }
                });

        alertBuilder.setNegativeButton("NEVERMIND",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openMainActivity();
                        toastMaker("Reservation was not cancelled");
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

    private void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private boolean validatePurchaseId(String input){
        try{
            int value = Integer.parseInt(input);
            return true;
        }catch(NumberFormatException e){
            toastMaker("Invalid Purchase Id");
            return false;
        }
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void refreshDisplay(){
        mPurchases = mUserDAO.getAllPurchasesByUserId(mUserId);

        if(mPurchases.size() <= 0){
            mPurchaseDisplay.setText("Ready to Search!");
        }

        StringBuilder sb = new StringBuilder();
        for(Purchase purchase : mPurchases){
            sb.append(purchase);
            sb.append("\n");
            sb.append("-----------");
            sb.append("\n");
        }

        mPurchaseDisplay.setText(sb.toString());
    }

    public void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void openFlightActivity(){
        Intent intent = new Intent(this,FlightActivity.class);
        startActivity(intent);
    }

    private void toastMaker(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
