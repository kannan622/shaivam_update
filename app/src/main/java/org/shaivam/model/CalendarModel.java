package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("calendar_fromdate")
    @Expose
    private String calendarFromdate;
    @SerializedName("calendar_title")
    @Expose
    private String calendarTitle;
    @SerializedName("calendar_descrption")
    @Expose
    private String calendarDescrption;
    @SerializedName("calendar_url")
    @Expose
    private String calendarUrl;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("createdate")
    @Expose
    private String createdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalendarFromdate() {
        return calendarFromdate;
    }

    public void setCalendarFromdate(String calendarFromdate) {
        this.calendarFromdate = calendarFromdate;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
    }

    public String getCalendarDescrption() {
        return calendarDescrption;
    }

    public void setCalendarDescrption(String calendarDescrption) {
        this.calendarDescrption = calendarDescrption;
    }

    public String getCalendarUrl() {
        return calendarUrl;
    }

    public void setCalendarUrl(String calendarUrl) {
        this.calendarUrl = calendarUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

}