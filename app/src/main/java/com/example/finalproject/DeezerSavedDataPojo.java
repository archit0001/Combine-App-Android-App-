package com.example.finalproject;

public class DeezerSavedDataPojo {
    public long id;
    public String album;
    public String title;
    public String duration;

    public DeezerSavedDataPojo(long id, String album, String title, String duration) {
        this.id = id;
        this.album = album;
        this.title = title;
        this.duration = duration;
    }

    public DeezerSavedDataPojo(String album, String title, String duration) {
        this.album = album;
        this.title = title;
        this.duration = duration;
    }

    /**
     * Constructor:
     */


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}