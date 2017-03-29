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

public class OrganizationsignUp extends AppCompatActivity {
    private EditText nameEditText, categoryEditText, addressEditText, passwordEditText, emailEditText,
            introEditText, employeeCountEditText;
    private Button submitButton;
    private String name, category, address, password, email, intro;
    private int employeeCount;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_sign_up);
        nameEditText = (EditText) findViewById(R.id.organizationNameEditText);
        categoryEditText = (EditText) findViewById(R.id.organizationCategoryEditText);
        addressEditText = (EditText) findViewById(R.id.organizationAddressEditText);
        passwordEditText = (EditText) findViewById(R.id.organizationPasswordEditText);
        emailEditText = (EditText) findViewById(R.id.organizationEmailEditText);
        introEditText = (EditText) findViewById(R.id.organizationIntroEditText);
        employeeCountEditText = (EditText) findViewById(R.id.organizationEmployeeCountEditText);
        submitButton = (Button) findViewById(R.id.organizationSubmitButton);
        pref = getApplicationContext().getSharedPreferences("UmeedPref", 0);
        editor = pref.edit();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    name = nameEditText.getText().toString();
                    category = categoryEditText.getText().toString();
                    address = addressEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    employeeCount = Integer.parseInt(employeeCountEditText.getText().toString());
                    intro = introEditText.getText().toString();
                    editor.putString("id", email);
                    editor.putString("password", password);
                    editor.commit();
                } else
                    Toast.makeText(OrganizationsignUp.this, "Please input all fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate() {
        if (nameEditText.getText().toString().isEmpty())
            return false;
        if (addressEditText.getText().toString().isEmpty())
            return false;
        if (categoryEditText.getText().toString().isEmpty())
            return false;
        if (employeeCountEditText.getText().toString().isEmpty())
            return false;
        if (introEditText.getText().toString().isEmpty())
            return false;
        if (passwordEditText.getText().toString().isEmpty())
            return false;
        if (emailEditText.getText().toString().isEmpty())
            return false;
        return true;
    }
}