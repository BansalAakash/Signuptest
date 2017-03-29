package com.example.aakas.signuptest;

import java.util.List;

/**
 * Created by aakas on 29-03-2017.
 */

public class YoutubeVideo {
    private String title;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private String description;
    private String status;
    List<String> tags;

    public YoutubeVideo(String title, String description, String status, List<String> tags, String uri) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.tags = tags;
        this.uri = uri;
    }
}
