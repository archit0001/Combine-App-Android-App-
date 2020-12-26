package com.example.finalproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Match implements Serializable {

    int id;
    String hls_url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("embed")
    @Expose
    private String embed;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("side1")
    @Expose
    private Side side1;
    @SerializedName("side2")
    @Expose
    private Side side2;
    @SerializedName("competition")
    @Expose
    private Competition competition;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHls_url() {
        return hls_url;
    }

    public void setHls_url(String hls_url) {
        this.hls_url = hls_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Side getSide1() {
        return side1;
    }

    public void setSide1(Side side1) {
        this.side1 = side1;
    }

    public Side getSide2() {
        return side2;
    }

    public void setSide2(Side side2) {
        this.side2 = side2;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }


    public class Video implements Serializable {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("embed")
        @Expose
        private String embed;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEmbed() {
            return embed;
        }

        public void setEmbed(String embed) {
            this.embed = embed;
        }

    }

}

