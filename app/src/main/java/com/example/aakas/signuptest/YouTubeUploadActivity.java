package com.example.aakas.signuptest;

import android.*;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by aakas on 29-03-2017.
 */

public class YouTubeUploadActivity extends AppCompatActivity {
    private static final String TAG = "YouTubeUploadActivity.java";
    private static final int IMAGE_PICKER_SELECT = 5;
    private Button selectVideoButton, uploadVideoButton;
    private EditText titleEditText, desciptionEditText, tagsEditText;
    private RadioButton publicRadioButton, privateRadioButton;
    private String videoTitle, videoDescription, viddeoStatus, videoUri;
    private YoutubeVideo youtubeVideo;
    List<String> videoTags;
    private GoogleAccountCredential credential;
    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;
    private TextView videoIdTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_youtube);
        checkPermissions();
        selectVideoButton = (Button) findViewById(R.id.selectVideoButton);
        uploadVideoButton = (Button) findViewById(R.id.uploadVideoButton);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        desciptionEditText = (EditText) findViewById(R.id.videoDescriptionEditText);
        tagsEditText = (EditText) findViewById(R.id.videoTagsEditText);
        publicRadioButton = (RadioButton) findViewById(R.id.publicRadioButton);
        privateRadioButton = (RadioButton) findViewById(R.id.privateRadioButton);
        videoIdTextView = (TextView) findViewById(R.id.videoIdTextView);
        videoTags = new ArrayList<String>();
        credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(
                YouTubeScopes.YOUTUBE_UPLOAD));
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
            }
        });
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    videoTitle = titleEditText.getText().toString();
                    videoDescription = desciptionEditText.getText().toString();
                    videoTags.addAll(Arrays.asList(tagsEditText.getText().toString().split(",")));
                    if (publicRadioButton.isChecked())
                        viddeoStatus = "public";
                    else if (privateRadioButton.isChecked())
                        viddeoStatus = "private";
                    youtubeVideo = new YoutubeVideo(videoTitle, videoDescription, viddeoStatus, videoTags, videoUri);
                    UploadVideo myAsyncTask = new UploadVideo(credential, youtubeVideo, YouTubeUploadActivity.this);
                    videoTitle = "";
                    videoDescription = "";
                    viddeoStatus = "";
                    videoUri = "";
                    videoTags.clear();
                    myAsyncTask.execute(YouTubeUploadActivity.this);
                }
            }
        });
    }

    public void doSomething(String text) {
        videoIdTextView.setText(text);
    }

    public boolean validate() {
        if (titleEditText.getText().toString().isEmpty()) {
            Toast.makeText(YouTubeUploadActivity.this, "Please specify video title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (desciptionEditText.getText().toString().isEmpty()) {
            Toast.makeText(YouTubeUploadActivity.this, "Please specify video description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tagsEditText.getText().toString().isEmpty()) {
            Toast.makeText(YouTubeUploadActivity.this, "Please specify video tags", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (videoUri.isEmpty()) {
            Toast.makeText(YouTubeUploadActivity.this, "Please select video", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
//                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString("preferredAccount", accountName);
//                        editor.commit();

                        Log.d("HelloActivity", credential.getSelectedAccountName() + "__________");
//                        if (validate()) {
//                            youtubeVideo = new YoutubeVideo(videoTitle, videoDescription, viddeoStatus, videoTags, videoUri);
//                            UploadVideo myAsyncTask = new UploadVideo(credential, youtubeVideo);
//                            myAsyncTask.execute(this);
//                        }
                    }
                }
            case REQUEST_AUTHORIZATION:
                if (resultCode != Activity.RESULT_OK) {
                    chooseAccount();
                }
                break;
            case IMAGE_PICKER_SELECT:
                if (resultCode == RESULT_OK) {

                    Uri selectedImageUri = data.getData();

                    // OI FILE Manager
                    String filemanagerstring = selectedImageUri.getPath();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);
                    Toast.makeText(YouTubeUploadActivity.this, selectedImagePath, Toast.LENGTH_LONG).show();
                    videoUri = selectedImagePath;

//                    Uri selectedMediaUri = data.getData();
//                    String uri1 = selectedMediaUri.toString();
//                    if (uri1.contains("video")) {
//                        //handle video
//                        videoUri = uri1;
//                        Log.d("Youtubeupload", uri1 + "  checkit");
//
//                    }
                }
        }

    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void checkPermissions() {
        int accountManagerPermission, getAccountsPermission, internetPermission, readExternalStoragePermission;
        accountManagerPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCOUNT_MANAGER);
        getAccountsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);
        internetPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        readExternalStoragePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (accountManagerPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCOUNT_MANAGER},
                    1);
        }
        if (getAccountsPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                    1);
        }
        if (readExternalStoragePermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        if (internetPermission
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    1);
        }
    }
}