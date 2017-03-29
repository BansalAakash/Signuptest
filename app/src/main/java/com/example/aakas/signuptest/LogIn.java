package com.example.aakas.signuptest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aakas on 29-03-2017.
 */

public class LogIn extends AppCompatActivity {
    private EditText passwordEditText, emailEditText;
    private TextView redirectTextView;
    private Button submitLogInButton;
    private String password, email;
    SharedPreferences pref;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        redirectTextView = (TextView) findViewById(R.id.redirectSignUpTextView);
        passwordEditText = (EditText) findViewById(R.id.passwordLogInEditText);
        emailEditText = (EditText) findViewById(R.id.emailLogInEditText);
        submitLogInButton = (Button) findViewById(R.id.logInSubmitButton);
        pref = getApplicationContext().getSharedPreferences("UmeedPref", 0);
        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        submitLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    password = passwordEditText.getText().toString();
                    email = emailEditText.getText().toString();
//                    TODO : put REST calls and check user credentials here
//                    if (credentials math) {
                    session.createLoginSession(email, password);
                    Toast.makeText(LogIn.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    finish();

//                }
//                else {
                    Toast.makeText(LogIn.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
//                }
                } else
                    Toast.makeText(LogIn.this, "Please input all fields!", Toast.LENGTH_SHORT).

                            show();
            }
        });
        redirectTextView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogIn.this, SignUpChoosingActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean validate() {
        if (passwordEditText.getText().toString().isEmpty())
            return false;
        if (emailEditText.getText().toString().isEmpty())
            return false;
        return true;
    }
}
