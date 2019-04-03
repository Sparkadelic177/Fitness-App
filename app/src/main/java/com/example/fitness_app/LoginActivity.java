package com.example.fitness_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    ImageView icon;
    EditText tvUsername;
    EditText tvPassword;
    TextView login;
    Button btnSignup;
    Intent gotoSignup;
    Intent gotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gotoSignup = new Intent(this, SignupActivity.class);
        gotoMain = new Intent(this, MainActivity.class);

        //referencing all the icons
        icon = findViewById(R.id.ivWalkLogo);
        tvPassword = findViewById(R.id.tvPassword);
        tvUsername = findViewById(R.id.tvUserName);
        btnSignup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.tvLogin);

        //setting up the vector image
        icon.setImageResource(R.drawable.ic_walk);


        //setting up actions
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString();
                String password = tvPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(gotoSignup);
            }
        });
    }

    //helper login function
    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if( e != null){
                    Log.e("failedLogin", "Something went wrong use the login: " + e.getMessage());
                    Toast.makeText(LoginActivity.this,"Something went wrong try again!" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                Log.d("successLogin", "We have successfully Logged in");
                Toast.makeText(LoginActivity.this,"Welcome!" , Toast.LENGTH_SHORT).show();
                startActivity(gotoMain);
                finish(); //close current activity in background
            }
        });
    }
}
