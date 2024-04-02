package com.goodforallcode.newmusicseeker.model;

public class RESTCallResult {
    String artist;
    String album;
    String track;
    String cover;

    public RESTCallResult(String artist, String album, String track, String cover) {
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.cover = cover;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
