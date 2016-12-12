package com.danieljdt.androidnapod.models;

/**
 * Created by danjdt on 08/12/2016.
 */

public class Picture {
    private String copyright;
    private String explanation;
    private String hdurl;
    private String url;
    private String title;
    private String date;
    private String media_type;

    public Picture(String copyright, String explanation, String hdurl, String url, String title, String date, String media_type) {
        this.copyright = copyright;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.url = url;
        this.title = title;
        this.date = date;
        this.media_type = media_type;
    }

    public Picture() {
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
}