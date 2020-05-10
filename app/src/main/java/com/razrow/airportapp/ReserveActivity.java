
package com.razrow.airportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveActivity extends AppCompatActivity {
    private EditText mDeparture;
    private EditText mArrival;
    private EditText mTickets;
    private TextView mFlightDisplay;
    private Button addButton;

    private String departing;
    private String arriving;
    private static int tickets;

    private Button mSearch;

    private Flight mFlight;
    List<Flight> mFlights;

    private UserDAO mUserDAO;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        getDatabase();

        mFlightDisplay = findViewById(R.id.flightDisplay);
        mFlightDisplay.setMovementMethod(new ScrollingMovementMethod());
        mFlightDisplay.setTextIsSelectable(true);
        addButton = findViewById(R.id.buttonAddFlight);
        addButton.setEnabled(false);

        mDeparture = findViewById(R.id.departure);
        mArrival = findViewById(R.id.arrival);
        mTickets = findViewById(R.id.tickets);
        mSearch = findViewById(R.id.buttonSearch);

        refreshDisplay();

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputLetters(mDeparture.getText().toString()) &&
                        validateInputLetters(mArrival.getText().toString()) &&
                        validateInputTickets(mTickets.getText().toString())) {
                    getValuesFromDisplay();
                }
                mFlights = mUserDAO.getFlightsByDepartureArrival(departing, arriving);
                refreshDisplay();
                if (mFlights.isEmpty()) {
                    alertNoFlights();
                }
            }
        });

        mSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openMainActivity();
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMaker();
            }
        });
    }

    private void refreshDisplay() {
        mFlights = mUserDAO.getFlightsByDepartureArrival(departing, arriving);

        if (mFlights.size() <= 0) {
            mFlightDisplay.setText("Ready to Search!");
        }

        if (mFlights.size() >= 1) {
            addButton.setEnabled(true);
        }

        StringBuilder sb = new StringBuilder();
        for (Flight flight : mFlights) {
            sb.append(flight);
            sb.append("\n");
            sb.append("-----------");
            sb.append("\n");
        }

        mFlightDisplay.setText(sb.toString());

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private boolean validateInputLetters(String input) {
        char letterCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if ((input.charAt(i) <= 90 && input.charAt(i) >= 65) ||
                    (input.charAt(i) <= 122 && input.charAt(i) >= 97) ||
                    (input.charAt(i) == 32)) {
                letterCount++;
            }
        }
        if (letterCount == input.length()) {
            return true;
        } else {
            toastMaker("Invalid Departure or Arrival Location");
            return false;
        }
    }

    private boolean validateInputTickets(String input) {
        try {
            int value = Integer.parseInt(input);
            if (value > 7) {
                toastMaker("Please, no more than 7 tickets in a purchase.");
                return false;
            }
            if (value < 1) {
                toastMaker("Invalid Ticket Amount");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            toastMaker("Invalid Ticket Amount");
            return false;
        }
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("tickets", tickets);
        return intent;
    }

    private void toastMaker(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void getValuesFromDisplay() {
        departing = mDeparture.getText().toString();
        arriving = mArrival.getText().toString();
        tickets = Integer.parseInt(mTickets.getText().toString());
        mFlight = mUserDAO.getFlightByDepartureArrival(departing, arriving);
    }

    private void alertMaker() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ReserveActivity.this);
        alertBuilder.setMessage("Purchase?");

        alertBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ReserveActivity.intentFactory(getApplicationContext());
                        startActivity(intent);
                        toastMaker("Please Login");
                    }
                });

        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openMainActivity();
                        toastMaker("Flight Was not purchased");
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

    private void alertNoFlights() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ReserveActivity.this);
        alertBuilder.setMessage("No Flights Found?");

        alertBuilder.setPositiveButton("EXIT?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openMainActivity();
                    }
                });

        alertBuilder.setNegativeButton("Search Again?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openReserveActivity();
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

    public void openReserveActivity() {
        Intent intent = new Intent(this, ReserveActivity.class);
        startActivity(intent);
    }
}
