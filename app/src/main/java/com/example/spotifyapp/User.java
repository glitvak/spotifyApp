package com.example.spotifyapp;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class User {
    private String name;
    private String authID;
    private List<Playlist> userPlaylists;
    private List<Song> likedHistory = Collections.emptyList();
    private List<Song> dislikedHistory= Collections.emptyList();
    private List<Song> topTracks;
    private List<Artist> topArtists;
    private Date lastAccess;

    public User(String name, String authID, List<Song> topTracks, List<Artist> topArtists) {
        this.name = name;
        this.authID = authID;
        this.topTracks = topTracks;
        this.topArtists = topArtists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public List<Playlist> getUserPlaylists() {
        return userPlaylists;
    }

    public void setUserPlaylists(List<Playlist> userPlaylists) {
        this.userPlaylists = userPlaylists;
    }

    public List<Song> getLikedHistory() {
        return likedHistory;
    }

    public void setLikedHistory(List<Song> likedHistory) {
        this.likedHistory = likedHistory;
    }

    public List<Song> getDislikedHistory() {
        return dislikedHistory;
    }

    public void setDislikedHistory(List<Song> dislikedHistory) {
        this.dislikedHistory = dislikedHistory;
    }

    public List<Song> getTopTracks() {
        return topTracks;
    }

    public void setTopTracks(List<Song> topTracks) {
        this.topTracks = topTracks;
    }

    public List<Artist> getTopArtists() {
        return topArtists;
    }

    public void setTopArtists(List<Artist> topArtists) {
        this.topArtists = topArtists;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
}
