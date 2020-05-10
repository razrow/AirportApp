package com.razrow.airportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

import java.util.List;
import java.util.regex.Pattern;

public class AccountActivity extends AppCompatActivity {
    private TextView mMainDisplay;
    private EditText mUsername;
    private EditText mPassword;
    private Button mButton;

    UserDAO mUserDAO;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;

    private User mUser;

    private Event mEvent;

    private static final String USER_ID_KEY = "com.razrow.airportapp.userIdKey";
    private static final String PREFERENCES_KEY = "com.razrow.airportapp.PREFERENCES_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getDatabase();

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mButton = findViewById(R.id.buttonCreate);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = getValuesFromDisplay();
                try{checkUser(newUser);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                openMainActivity();
            }
        });

    }

    private void addUserToPrefs(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY,userId);
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void checkUser(User user){
        if(!currentUser(user)){
            toastMaker("Username already taken.");
        }else{
            if(validateUsername(user.getUsername())){
                if (validatePassword(user.getPassword())) {
                    mUserDAO.insert(createEvent(user));
                    mUserDAO.insert(user);
                    toastMaker("Created A User:\n" + user.toString());
                } else {
                    toastMaker("Invalid password. Passwords need 3 letters and 3 numbers");
                }
            }else{
                toastMaker("Invalid Username. Usernames need letters and numbers");
            }
        }
    }

    private boolean validatePassword(String password){
        int numCount = 0;
        int letterCount = 0;
        for(int i = 0; i<password.length(); i++){
            if((password.charAt(i) <= 90 && password.charAt(i) >= 65) ||
                    (password.charAt(i) <= 122 && password.charAt(i) >= 97)){
                letterCount++;
            }
            if((password.charAt(i) <= 57 && password.charAt(i) >= 48)){
                numCount++;
            }
        }
        if(numCount == 3 && letterCount==3){
            return true;
        }else{
            return false;
        }
    }

    private boolean validateUsername(String username){
        int numCount = 0;
        int letterCount = 0;
        for(int i = 0; i<username.length(); i++){
            if((username.charAt(i) <= 90 && username.charAt(i) >= 65) ||
                    (username.charAt(i) <= 122 && username.charAt(i) >= 97)){
                letterCount++;
            }
            if((username.charAt(i) <= 57 && username.charAt(i) >= 48)){
                numCount++;
            }
        }
        if(numCount >= 1 && letterCount >= 1){
            return true;
        }else{
            return false;
        }
    }

    private Event createEvent(User user){
        Event event = new Event("Created An Account\n" + user.toString());
        return event;
    }

    private boolean currentUser(User user){
        if(mUserDAO.getUserByUsername(user.getUsername()) == null)
            return true;
        else
            return false;
    }

    public void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
    }

    private void toastMaker(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private User getValuesFromDisplay(){
        String username;
        String password;

        username = mUsername.getText().toString();
        password = mPassword.getText().toString();

        User newUser = new User(username,password,false);
        return newUser;
    }

    public String returnValuesFromDisplay(User user){
        final String message = user.toString();
        return message;
    }

    private void openMainActivity(){
        Intent intent = new Intent(AccountActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
