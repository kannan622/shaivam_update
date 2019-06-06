package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentRadio {

    @SerializedName("radio_id")
    @Expose
    private Integer radioId;
    @SerializedName("radio_title")
    @Expose
    private String radioTitle;
    @SerializedName("radio1_title")
    @Expose
    private String radio1Title;
    @SerializedName("radio2_title")
    @Expose
    private String radio2Title;
    @SerializedName("radio_titles")
    @Expose
    private String radioTitles;
    @SerializedName("radio_date")
    @Expose
    private String radioDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Timestart")
    @Expose
    private String timestart;
    @SerializedName("Timeend")
    @Expose
    private String timeend;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Program")
    @Expose
    private String program;
    @SerializedName("Artist")
    @Expose
    private String artist;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("fk_user_id")
    @Expose
    private Integer fkUserId;
    @SerializedName("Created_date")
    @Expose
    private String createdDate;

    public Integer getRadioId() {
        return radioId;
    }

    public void setRadioId(Integer radioId) {
        this.radioId = radioId;
    }

    public String getRadioTitle() {
        return radioTitle;
    }

    public void setRadioTitle(String radioTitle) {
        this.radioTitle = radioTitle;
    }

    public String getRadio1Title() {
        return radio1Title;
    }

    public void setRadio1Title(String radio1Title) {
        this.radio1Title = radio1Title;
    }

    public String getRadio2Title() {
        return radio2Title;
    }

    public void setRadio2Title(String radio2Title) {
        this.radio2Title = radio2Title;
    }

    public String getRadioTitles() {
        return radioTitles;
    }

    public void setRadioTitles(String radioTitles) {
        this.radioTitles = radioTitles;
    }

    public String getRadioDate() {
        return radioDate;
    }

    public void setRadioDate(String radioDate) {
        this.radioDate = radioDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Integer fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}