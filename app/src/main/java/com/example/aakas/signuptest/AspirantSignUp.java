package com.example.aakas.signuptest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

public class AspirantSignUp extends AppCompatActivity {
    private EditText nameEditText, educationEditText, addressEditText, passwordEditText, emailEditText;
    private static Button submitButton, dateButton;
    private String name, education, address, password, email, gender;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Date dob;
    private RadioButton mRadio, fRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspirant_sign_up);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        educationEditText = (EditText) findViewById(R.id.educationEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        dateButton = (Button) findViewById(R.id.datePickerButton);
        mRadio = (RadioButton) findViewById(R.id.mRadioButton);
        fRadio = (RadioButton) findViewById(R.id.fRadioButton);
        pref = getApplicationContext().getSharedPreferences("UmeedPref", 0);
        editor = pref.edit();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    name = nameEditText.getText().toString();
                    education = educationEditText.getText().toString();
                    address = addressEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    if (mRadio.isChecked())
                        gender = "male";
                    else
                        gender = "female";
                    dob = Date.valueOf(dateButton.getText().toString());

                    editor.putString("id", email);
                    editor.putString("password", password);
                    editor.commit();
                } else
                    Toast.makeText(AspirantSignUp.this, "Please input all fields!", Toast.LENGTH_SHORT).show();
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    public boolean validate() {
        if (nameEditText.getText().toString().isEmpty())
            return false;
        if (educationEditText.getText().toString().isEmpty())
            return false;
        if (addressEditText.getText().toString().isEmpty())
            return false;
        if (emailEditText.getText().toString().isEmpty())
            return false;
        if (passwordEditText.getText().toString().isEmpty())
            return false;
        if (dateButton.getText().toString().isEmpty())
            return false;
        return true;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = 2000;
            int month = 1;
            int day = 1;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateButton.setText(new Date(year - 1900, month, day).toString());
        }
    }

    public void createLoginSession(String email, String password) {
        // Storing login value as TRUE
        editor.putBoolean("loginStatus", true);

        // Storing name in pref
        editor.putString("email", email);

        // Storing email in pref
        editor.putString("password", password);

        // commit changes
        editor.commit();
    }
}