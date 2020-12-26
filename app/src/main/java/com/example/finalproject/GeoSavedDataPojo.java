package com.example.finalproject;

public class GeoSavedDataPojo {
    public long id;
    public String country;
    public String region;
    public String city;
    public String currency;
    public String Latitude;
    public String longitude;

    public GeoSavedDataPojo(long id, String country, String region, String city, String currency, String latitude, String longitude) {
        this.id = id;
        this.country = country;
        this.region = region;
        this.city = city;
        this.currency = currency;
        Latitude = latitude;
        this.longitude = longitude;
    }

    public GeoSavedDataPojo(String country, String region, String city, String currency, String latitude, String longitude) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.currency = currency;
        Latitude = latitude;
        this.longitude = longitude;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Constructor:
     */


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}