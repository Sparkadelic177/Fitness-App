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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    ImageView icon;
    EditText tvUsername;
    EditText tvPassword;
    EditText tvEmail;
    TextView login;
    Button btnSignup;
    Intent gotoLogin;
    Intent gotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        gotoLogin = new Intent(this, LoginActivity.class);
        gotoMain = new Intent(this, MainActivity.class);

        icon = findViewById(R.id.ivWalkLogo);
        icon.setImageResource(R.drawable.ic_walk);

        tvEmail = findViewById(R.id.tvEmail);
        tvUsername = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);

        btnSignup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.tvLogin);

        //setting up the buttons
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getText().toString();
                String username = tvUsername.getText().toString();
                String password = tvPassword.getText().toString();
                signUp(email,username,password);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(gotoLogin);
            }
        });

    }

    private void signUp(String email, String username, String password) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("failedSignup", "Something happend in sign process " + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(SignupActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                startActivity(gotoMain);
            }
        });
    }
}
