package com.example.spotifyapp;

import java.util.List;

public class Song {
    private String name;
    private Artist artist;
    private String genre;
    private int length;
    private List<String> analysis;

    public Song(String name, Artist artist, int length, List<String> analysis) {
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.analysis = analysis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(List<String> analysis) {
        this.analysis = analysis;
    }
}
