package org.shaivam.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Synchronize implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("version")
    @Expose
    private Integer version;
    private final static long serialVersionUID = -864750963868849679L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Synchronize() {
    }

    /**
     *
     * @param id
     * @param url
     * @param version
     */
    public Synchronize(Integer id, String url, Integer version) {
        super();
        this.id = id;
        this.url = url;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}