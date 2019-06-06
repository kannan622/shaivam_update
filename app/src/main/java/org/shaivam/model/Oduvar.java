package org.shaivam.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Oduvar implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("odhuvarname")
    @Expose
    private String odhuvarname;
    @SerializedName("odhuvartamilname")
    @Expose
    private String odhuvartamilname;
    @SerializedName("thirumuraiId")
    @Expose
    private Integer thirumuraiId;
    @SerializedName("thirumurai")
    @Expose
    private String thirumurai;
    @SerializedName("pathikamNo")
    @Expose
    private Integer pathikamNo;
    @SerializedName("thirumariasiriyar")
    @Expose
    private String thirumariasiriyar;
    @SerializedName("pathigam")
    @Expose
    private String pathigam;
    @SerializedName("audiourl")
    @Expose
    private String audiourl;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("updatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;
    private final static long serialVersionUID = -2505185035051195614L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Oduvar() {
    }

    /**
     *
     * @param odhuvarname
     * @param thirumuraiId
     * @param status
     * @param pathikamNo
     * @param updatedBy
     * @param audiourl
     * @param id
     * @param createdOn
     * @param thirumurai
     * @param createdBy
     * @param prefix
     * @param pathigam
     * @param odhuvartamilname
     * @param thirumariasiriyar
     * @param updatedOn
     */
    public Oduvar(Integer id, String prefix, String odhuvarname, String odhuvartamilname,
                  Integer thirumuraiId, String thirumurai, Integer pathikamNo, String thirumariasiriyar,
                  String pathigam, String audiourl, Integer status, String createdOn, String createdBy,
                  String updatedOn, String updatedBy) {
        super();
        this.id = id;
        this.prefix = prefix;
        this.odhuvarname = odhuvarname;
        this.odhuvartamilname = odhuvartamilname;
        this.thirumuraiId = thirumuraiId;
        this.thirumurai = thirumurai;
        this.pathikamNo = pathikamNo;
        this.thirumariasiriyar = thirumariasiriyar;
        this.pathigam = pathigam;
        this.audiourl = audiourl;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getOdhuvarname() {
        return odhuvarname;
    }

    public void setOdhuvarname(String odhuvarname) {
        this.odhuvarname = odhuvarname;
    }

    public String getOdhuvartamilname() {
        return odhuvartamilname;
    }

    public void setOdhuvartamilname(String odhuvartamilname) {
        this.odhuvartamilname = odhuvartamilname;
    }

    public Integer getThirumuraiId() {
        return thirumuraiId;
    }

    public void setThirumuraiId(Integer thirumuraiId) {
        this.thirumuraiId = thirumuraiId;
    }

    public String getThirumurai() {
        return thirumurai;
    }

    public void setThirumurai(String thirumurai) {
        this.thirumurai = thirumurai;
    }

    public Integer getPathikamNo() {
        return pathikamNo;
    }

    public void setPathikamNo(Integer pathikamNo) {
        this.pathikamNo = pathikamNo;
    }

    public String getThirumariasiriyar() {
        return thirumariasiriyar;
    }

    public void setThirumariasiriyar(String thirumariasiriyar) {
        this.thirumariasiriyar = thirumariasiriyar;
    }

    public String getPathigam() {
        return pathigam;
    }

    public void setPathigam(String pathigam) {
        this.pathigam = pathigam;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}