package com.example.spotifyapp.Model;

public class Playlist {
    private String name;
    private String link;
    private String image;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() { return link; }

    public void setLink(String link) {
        this.link = link;
    }
    public String getURL() { return image; }

    public void setURL(String image) {
        this.image = image;
    }
}
