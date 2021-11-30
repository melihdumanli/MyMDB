package com.bilgeadam.clientside.dto;

import com.bilgeadam.globalization.Globalization;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class MovieDto {
    //MovieDTO Properties
    private String title;
    private String year;
    private String genre;
    private double maxRating;
    private double minRating;
    private Date maxTimeStamp;
    private Date minTimeStamp;

    private static ResourceBundle bundle = Globalization.getLanguageBundle();

    //MovieDTO Constructors
    public MovieDto() {
    }

    //MovieDTO Constructor with parameters
    public MovieDto(String title, String year, String genre, double maxRating, double minRating, Date maxTimeStamp, Date minTimeStamp) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.maxRating = maxRating;
        this.minRating = minRating;
        this.maxTimeStamp = maxTimeStamp;
        this.minTimeStamp = minTimeStamp;
    }

    //MovieDTO toString method
    @Override
    public String toString() {
        return  bundle.getString("Globalization.MOVIE") + "{" +
                bundle.getString("Globalization.MOVIETITLE") + ": " + title + ", " +
                bundle.getString("Globalization.MOVIEYEAR") + ": " + year + ", " +
                bundle.getString("Globalization.MOVIEGENRE") + ": " + genre + ", " +
                bundle.getString("Globalization.MAXRATING") + ": " + maxRating + ", " +
                bundle.getString("Globalization.MAXTIMESTAMP") + ": " + maxTimeStamp + ", " +
                bundle.getString("Globalization.MINRATING") + ": " + minRating + ", " +
                bundle.getString("Globalization.MINTIMESTAMP") + ": " + minTimeStamp +
                '}';
    }

    //MovieDTO Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(double maxRating) {
        this.maxRating = maxRating;
    }

    public double getMinRating() {
        return minRating;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public Date getMaxTimeStamp() {
        return maxTimeStamp;
    }

    public void setMaxTimeStamp(Date maxTimeStamp) {
        this.maxTimeStamp = maxTimeStamp;
    }

    public Date getMinTimeStamp() {
        return minTimeStamp;
    }

    public void setMinTimeStamp(Date minTimeStamp) {
        this.minTimeStamp = minTimeStamp;
    }
}


