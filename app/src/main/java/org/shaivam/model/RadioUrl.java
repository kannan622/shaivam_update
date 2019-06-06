package org.shaivam.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RadioUrl implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("radioname")
    @Expose
    private String radioname;
    @SerializedName("radiourl")
    @Expose
    private String radiourl;
    private final static long serialVersionUID = 6289678679260123474L;

    /**
     * No args constructor for use in serialization
     */
    public RadioUrl() {
    }

    /**
     * @param id
     * @param radiourl
     * @param radioname
     */
    public RadioUrl(Integer id, String radioname, String radiourl) {
        super();
        this.id = id;
        this.radioname = radioname;
        this.radiourl = radiourl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRadioname() {
        return radioname;
    }

    public void setRadioname(String radioname) {
        this.radioname = radioname;
    }

    public String getRadiourl() {
        return radiourl;
    }

    public void setRadiourl(String radiourl) {
        this.radiourl = radiourl;
    }

}