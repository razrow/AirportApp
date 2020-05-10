package com.razrow.airportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razrow.airportapp.db.AppDatabase;
import com.razrow.airportapp.db.UserDAO;

public class ManageActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button Login;

    private User mUser;

    private UserDAO mUserDAO;

    private String mPasswordString;
    private String mUsernameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        getDatabase();

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        Login = findViewById(R.id.buttonLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (checkForUserInDatabase()){
                    if (validatePassword()) {
                        if(mUserDAO.getUserByUsername(mUsernameString).isAdmin()){
                            openLogActivity();
                        }else{
                            openMainActivity();
                            toastMaker("User does not have admin credentials. ");
                        }

                    } else {
                        openMainActivity();
                        toastMaker("Incorrect Password.");
                    }
                }else{
                    openMainActivity();
                }
            }
        });
    }

    private void toastMaker(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class,AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void getValuesFromDisplay(){
        mUsernameString = mUsername.getText().toString();
        mPasswordString = mPassword.getText().toString();
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPasswordString);
    }

    private boolean checkForUserInDatabase(){
        mUser = mUserDAO.getUserByUsername(mUsernameString);
        if(mUser == null){
            Toast.makeText(this,"no user " + mUsernameString + " found",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void openLogActivity(){
        Intent intent = new Intent(this,LogActivity.class);
        startActivity(intent);
    }
}
