package com.example.aakas.signuptest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by aakas on 26-03-2017.
 */

public class NGOSignUp extends AppCompatActivity {
    private EditText nameEditText, addressEditText, passwordEditText, emailEditText;
    private Button submitButton;
    private String name, address, password, email;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ngo_sign_up);
        nameEditText = (EditText) findViewById(R.id.ngoNameEditText);
        addressEditText = (EditText) findViewById(R.id.ngoAddressEditText);
        passwordEditText = (EditText) findViewById(R.id.ngoPasswordEditText);
        emailEditText = (EditText) findViewById(R.id.ngoemailAddressEditText);
        submitButton = (Button) findViewById(R.id.ngosubmitButton);
        pref = getApplicationContext().getSharedPreferences("UmeedPref", 0);
        editor = pref.edit();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    name = nameEditText.getText().toString();
                    address = addressEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    editor.putString("id", email);
                    editor.putString("password", password);
                    editor.commit();
                } else
                    Toast.makeText(NGOSignUp.this, "Please input all fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate() {
        if (nameEditText.getText().toString().isEmpty())
            return false;
        if (addressEditText.getText().toString().isEmpty())
            return false;
        if (passwordEditText.getText().toString().isEmpty())
            return false;
        if (emailEditText.getText().toString().isEmpty())
            return false;
        return true;
    }
}