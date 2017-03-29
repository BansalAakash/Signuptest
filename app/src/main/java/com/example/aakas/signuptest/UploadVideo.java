/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.aakas.signuptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.aakas.signuptest.YouTubeUploadActivity.REQUEST_AUTHORIZATION;

public class UploadVideo extends AsyncTask<Context, Void, Void> {

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;
    private Context context;

    private static final String VIDEO_FILE_FORMAT = "video/*";
    private static final String TAG = "UploadVideo.java";
    private GoogleAccountCredential credential;
    private YoutubeVideo video;
    private static final String SUCCEEDED = "succeeded";
    private String returnVideoId;


    public YouTubeUploadActivity activity;

    public UploadVideo(GoogleAccountCredential tempCredential, YoutubeVideo video, YouTubeUploadActivity activity) {
        super();
        this.credential = tempCredential;
        this.video = video;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Context... params) {
        this.context = params[0];
        Log.v("UploadVideo.java", "Here************");
        try {

            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            Log.d("UploadVideo.java", credential.getSelectedAccountName() + "!!!!!!!!!!");
//             YouTube client

            youtube = new YouTube.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Umeed 1.0").build();
            Log.d("UploadVideo.java", credential.getSelectedAccountName() + "|||||||||||||||||");

            Log.v("UploadVideo.java", "0************");

            System.out.println("Uploading video");

            Video videoObjectDefiningMetadata = new Video();

            // Set the video to be publicly visible. This is the default
            // setting. Other supporting settings are "unlisted" and "private."
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus(video.getStatus());
            videoObjectDefiningMetadata.setStatus(status);

            // Most of the video's metadata is set on the VideoSnippet object.
            VideoSnippet snippet = new VideoSnippet();

            snippet.setTitle(video.getTitle());
            snippet.setDescription(video.getDescription());
            Log.v("UploadVideo.java", "1************");
            List<String> tags = new ArrayList<String>();
            snippet.setTags(video.getTags());

            videoObjectDefiningMetadata.setSnippet(snippet);
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT,
                    new BufferedInputStream(new FileInputStream(video.getUri())));
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();

            YouTube.Videos.Insert videoInsert = youtube.videos()
                    .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
            Log.v("UploadVideo.java", "3************");

            uploader.setDirectUploadEnabled(true);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            Log.v("UploadVideo.java", "Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            Log.v("UploadVideo.java", "Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            Log.v("UploadVideo.java", "Upload in progress");
                            Log.v("UploadVideo.java", "Upload percentage: " + uploader.getNumBytesUploaded());
                            break;
                        case MEDIA_COMPLETE:
                            Log.v("UploadVideo.java", "Upload Completed!");
                            break;
                        case NOT_STARTED:
                            Log.v("UploadVideo.java", "Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);
            Log.v("UploadVideo.java", "4************");

            // Call the API and upload the video.
            Log.d("UploadVideo.java", "*****" + credential.getSelectedAccountName() + "*****");
            Video returnedVideo = videoInsert.execute();

            // Print data about the newly inserted video from the API response.
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
            Log.v("UploadVideo.java", "Success************");
            this.returnVideoId = returnedVideo.getId();
            activity.doSomething(this.returnVideoId);


        } catch (UserRecoverableAuthIOException userRecoverableException) {
            Log.i(TAG, String.format("UserRecoverableAuthIOException: %s",
                    userRecoverableException.getMessage() + "........"));
            ((SignUpChoosingActivity) context).startActivityForResult(userRecoverableException.getIntent(), REQUEST_AUTHORIZATION);
//            requestAuth(context, userRecoverableException);
        } catch (GoogleJsonResponseException e) {
            Log.v("UploadVideo.java", "5************");

            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.v("UploadVideo.java", "6************");
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            Log.v("UploadVideo.java", "7************");

            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
        return null;
    }


}
