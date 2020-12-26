package com.example.finalproject;

public class LyricsSavedDataPojo {
    public String artist;
    public String lyrics;
    public String title;
    public long id;


    /**
     * Constructor:
     */
    public LyricsSavedDataPojo(long id, String artist, String title, String lyrics) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }


    public LyricsSavedDataPojo(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}