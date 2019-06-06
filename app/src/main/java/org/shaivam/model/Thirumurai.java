package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "thirumurai")
public class Thirumurai {
    @DatabaseField(id = true, columnName = "id", canBeNull = false)
    @SerializedName("id")
    @Expose
    private Integer id;

    @DatabaseField(columnName = "thirumuraiNo")
    @SerializedName("thirumuraiNo")
    @Expose
    private int thirumuraiNo;
    @DatabaseField(columnName = "thirumuraiName")
    @SerializedName("thirumuraiName")
    @Expose
    private String thirumuraiName;
    @DatabaseField(columnName = "alternateName")
    @SerializedName("alternateName")
    @Expose
    private String alternateName;
    @DatabaseField(columnName = "thalam")
    @SerializedName("thalam")
    @Expose
    private String thalam;
    @DatabaseField(columnName = "pann")
    @SerializedName("pann")
    @Expose
    private String pann;
    @DatabaseField(columnName = "author")
    @SerializedName("author")
    @Expose
    private String author;
    @DatabaseField(columnName = "country")
    @SerializedName("country")
    @Expose
    private String country;
    @DatabaseField(columnName = "songUrl")
    @SerializedName("songUrl")
    @Expose
    private String songUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getThirumuraiNo() {
        return thirumuraiNo;
    }

    public void setThirumuraiNo(int thirumuraiNo) {
        this.thirumuraiNo = thirumuraiNo;
    }

    public String getThirumuraiName() {
        return thirumuraiName;
    }

    public void setThirumuraiName(String thirumuraiName) {
        this.thirumuraiName = thirumuraiName;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getThalam() {
        return thalam;
    }

    public void setThalam(String thalam) {
        this.thalam = thalam;
    }

    public String getPann() {
        return pann;
    }

    public void setPann(String pann) {
        this.pann = pann;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}