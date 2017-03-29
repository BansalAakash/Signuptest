package com.example.aakas.signuptest;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by aakas on 26-03-2017.
 */

public class SignUpChoosingActivity extends AppCompatActivity {
    private static final String TAG = "SignUpChoosingActivity.java";
    private Button ngo, organzation, aspirant;
    private TextView redirectLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_signup);
        ngo = (Button) findViewById(R.id.ngoChoiceButton);
        aspirant = (Button) findViewById(R.id.aspirantChoiceButton);
        organzation = (Button) findViewById(R.id.organizationChoiceButton);
        redirectLoginTextView = (TextView) findViewById(R.id.redirectLogInTextView);

//        UploadVideo.main(R.raw.logo_bis_audio);
        ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpChoosingActivity.this, NGOSignUp.class);
                startActivity(intent);
            }
        });
        organzation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpChoosingActivity.this, OrganizationsignUp.class);
                startActivity(intent);
            }
        });
        aspirant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpChoosingActivity.this, AspirantSignUp.class);
                startActivity(intent);
            }
        });
        redirectLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpChoosingActivity.this, LogIn.class);
                startActivity(i);
            }
        });
        checkPermissions();

    }

    private void checkPermissions() {
        int accountManagerPermission, getAccountsPermission, internetPermission, readExternalStoragePermission;
        accountManagerPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCOUNT_MANAGER);
        getAccountsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (accountManagerPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCOUNT_MANAGER},
                    1);
        }
        if (getAccountsPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    1);
        }
        if (readExternalStoragePermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        if (internetPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
    }
}
