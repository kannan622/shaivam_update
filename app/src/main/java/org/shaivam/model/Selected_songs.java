package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Selected_songs {
    @SerializedName("audioUrl")
    @Expose
    private String audioUrl;
    @SerializedName("author_name")
    @Expose
    private String author_name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thalam")
    @Expose
    private String thalam;
    public Selected_songs() {

    }

    public Selected_songs(String audioUrl, String author_name,
                          String title, String thalam) {
        this.audioUrl = audioUrl;
        this.author_name = author_name;
        this.title = title;
        this.thalam = thalam;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThalam() {
        return thalam;
    }

    public void setThalam(String thalam) {
        this.thalam = thalam;
    }
}
