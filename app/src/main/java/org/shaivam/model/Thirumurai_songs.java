package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@DatabaseTable(tableName = "thirumurai_songs")
public class Thirumurai_songs {
    @DatabaseField(id = true, columnName = "id", canBeNull = false)
    @SerializedName("id")
    @Expose
    private int id;
    @DatabaseField(columnName = "titleNo")
    @SerializedName("titleNo")
    @Expose
    private String titleNo;
    @DatabaseField(columnName = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @DatabaseField(columnName = "pann")
    @SerializedName("pann")
    @Expose
    private String pann;
    @DatabaseField(columnName = "audioUrl")
    @SerializedName("audioUrl")
    @Expose
    private String audioUrl;
    @DatabaseField(columnName = "song")
    @SerializedName("song")
    @Expose
    private String song;
    @DatabaseField(columnName = "thirumuraiId")
    @SerializedName("thirumuraiId")
    @Expose
    private int thirumuraiId;
    @DatabaseField(columnName = "refId")
    @SerializedName("refId")
    @Expose
    private int refId;
    @DatabaseField(columnName = "songNo")
    @SerializedName("songNo")
    @Expose
    private int songNo;
    @DatabaseField(columnName = "thalam")
    @SerializedName("thalam")
    @Expose
    private String thalam;
    @DatabaseField(columnName = "country")
    @SerializedName("country")
    @Expose
    private String country;
    @DatabaseField(columnName = "author")
    @SerializedName("author")
    @Expose
    private String author;
    @DatabaseField(columnName = "url")
    @SerializedName("url")
    @Expose
    private String url;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    @Expose
    private String type;
    @DatabaseField(columnName = "centerNo")
    @SerializedName("centerNo")
    @Expose
    private String centerNo;
    @DatabaseField(columnName = "addon")
    @SerializedName("addon")
    @Expose
    private String addon;

    @DatabaseField(columnName = "favorites")
    @SerializedName("favorites")
    @Expose
    private Boolean favorites;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(String titleNo) {
        this.titleNo = titleNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPann() {
        return pann;
    }

    public void setPann(String pann) {
        this.pann = pann;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getThirumuraiId() {
        return thirumuraiId;
    }

    public void setThirumuraiId(int thirumuraiId) {
        this.thirumuraiId = thirumuraiId;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public int getSongNo() {
        return songNo;
    }

    public void setSongNo(int songNo) {
        this.songNo = songNo;
    }

    public String getThalam() {
        return thalam;
    }

    public void setThalam(String thalam) {
        this.thalam = thalam;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCenterNo() {
        return centerNo;
    }

    public void setCenterNo(String centerNo) {
        this.centerNo = centerNo;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public Boolean getFavorites() {
        if (favorites != null)
            return favorites;
        else
            return false;
    }

    public void setFavorites(Boolean favorites) {
        this.favorites = favorites;
    }

    public static String formatDuration(final int duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    public static int formatTrack(int trackNumber) {
        int formatted = trackNumber;
        if (trackNumber >= 1000) {
            formatted = trackNumber % 1000;
        }
        return formatted;
    }
}