package com.bilgeadam.clientside.dto;

import com.bilgeadam.globalization.Globalization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ActorDto implements Serializable {
    //ActorDTO properties
    private String name;
    private String birthYear;
    private String deathYear;
    private String primaryProfession;
    private List<String> knownForTitles;

    private static ResourceBundle bundle = Globalization.getLanguageBundle();

    // Constructor without parameters
    public ActorDto() {
    }

    // Constructor with parameters
    public ActorDto(String name, String birthYear, String deathYear, String primaryProfession, List<String> knownForTitles) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
    }

    // toString method
    @Override
    public String toString() {
        return "Actor{" +
                bundle.getString("Globalization.NAME") + ": " + name +  ", " +
                bundle.getString("Globalization.BIRTHYEAR") +  ": " + birthYear + ", " +
                bundle.getString("Globalization.DEATHYEAR") +  ": " + deathYear + ", " +
                bundle.getString("Globalization.PROFESSION") +  ": " + primaryProfession +  ", " +
                bundle.getString("Globalization.KNOWNFOR") +  ": " + knownForTitles.toString() +
                '}';
    }

    // Getter and Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(String deathYear) {
        this.deathYear = deathYear;
    }

    public String getPrimaryProfession() {
        return primaryProfession;
    }

    public void setPrimaryProfession(String primaryProfession) {
        this.primaryProfession = primaryProfession;
    }

    public List<String> getKnownForTitles() {
        return knownForTitles;
    }

    public void setKnownForTitles(List<String> knownForTitles) {
        this.knownForTitles = knownForTitles;
    }

}
