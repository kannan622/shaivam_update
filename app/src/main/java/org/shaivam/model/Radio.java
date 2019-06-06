package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Radio implements Serializable {

    @SerializedName("radio_id")
    @Expose
    private String radioId;
    @SerializedName("Timestart")
    @Expose
    private String timestart;
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
    private String duration;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("headerid")
    @Expose
    private String headerid;
    @SerializedName("orderid")
    @Expose
    private String orderid;
    @SerializedName("updated")
    @Expose
    private String updated;
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
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Timeend")
    @Expose
    private String timeend;
    @SerializedName("fk_user_id")
    @Expose
    private String fkUserId;
    private final static long serialVersionUID = 1328991812730617560L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Radio() {
    }

    /**
     *
     * @param radioTitle
     * @param headerid
     * @param status
     * @param radioTitles
     * @param date
     * @param radio2Title
     * @param category
     * @param duration
     * @param radioDate
     * @param updated
     * @param program
     * @param fkUserId
     * @param timeend
     * @param radio1Title
     * @param radioId
     * @param timestart
     * @param artist
     * @param language
     * @param orderid
     * @param createdDate
     */
    public Radio(String radioId, String timestart, String category, String program, String artist, String language, String duration, String date, String headerid, String orderid, String updated, String radioTitle, String radio1Title, String radio2Title, String radioTitles, String radioDate, String createdDate, String status, String timeend, String fkUserId) {
        super();
        this.radioId = radioId;
        this.timestart = timestart;
        this.category = category;
        this.program = program;
        this.artist = artist;
        this.language = language;
        this.duration = duration;
        this.date = date;
        this.headerid = headerid;
        this.orderid = orderid;
        this.updated = updated;
        this.radioTitle = radioTitle;
        this.radio1Title = radio1Title;
        this.radio2Title = radio2Title;
        this.radioTitles = radioTitles;
        this.radioDate = radioDate;
        this.createdDate = createdDate;
        this.status = status;
        this.timeend = timeend;
        this.fkUserId = fkUserId;
    }

    public String getRadioId() {
        return radioId;
    }

    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeaderid() {
        return headerid;
    }

    public void setHeaderid(String headerid) {
        this.headerid = headerid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

}