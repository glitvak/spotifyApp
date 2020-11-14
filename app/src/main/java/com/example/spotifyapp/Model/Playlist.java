package com.example.spotifyapp.Model;

import com.example.spotifyapp.Model.Song;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Playlist {
    private String name;
    private Date createDate;
    private int id;
    private List<Song> tracks = Collections.emptyList();

    public Playlist(String name, Date createDate, int id) {
        this.name = name;
        this.createDate = createDate;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Song> getTracks() {
        return tracks;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }
}
