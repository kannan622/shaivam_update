package org.shaivam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Events {

    @SerializedName("festival_id")
    @Expose
    private Integer festivalId;
    @SerializedName("name_offestival")
    @Expose
    private String nameOffestival;
    @SerializedName("festival_url")
    @Expose
    private String festivalUrl;
    @SerializedName("festival_sdate")
    @Expose
    private String festivalSdate;
    @SerializedName("festival_edate")
    @Expose
    private String festivalEdate;
    @SerializedName("festival_description")
    @Expose
    private String festivalDescription;
    @SerializedName("festival_country")
    @Expose
    private String festivalCountry;
    @SerializedName("festival_state")
    @Expose
    private String festivalState;
    @SerializedName("festival_city")
    @Expose
    private String festivalCity;
    @SerializedName("festival_district")
    @Expose
    private String festivalDistrict;
    @SerializedName("festival_location")
    @Expose
    private String festivalLocation;
    @SerializedName("festival_zipcode")
    @Expose
    private String festivalZipcode;
    @SerializedName("festival_contactname")
    @Expose
    private String festivalContactname;
    @SerializedName("festival_contactmobile")
    @Expose
    private String festivalContactmobile;
    @SerializedName("festival_contactemail")
    @Expose
    private String festivalContactemail;
    @SerializedName("festival_contactaddress")
    @Expose
    private String festivalContactaddress;
    @SerializedName("festival_image")
    @Expose
    private String festivalImage;
    @SerializedName("festival_order")
    @Expose
    private String festivalOrder;
    @SerializedName("festival_category")
    @Expose
    private Integer festivalCategory;
    @SerializedName("fk_type_id")
    @Expose
    private Integer fkTypeId;
    @SerializedName("festival_seotitle")
    @Expose
    private String festivalSeotitle;
    @SerializedName("festival_seodes")
    @Expose
    private String festivalSeodes;
    @SerializedName("festival_seokey")
    @Expose
    private String festivalSeokey;
    @SerializedName("fk_user_id")
    @Expose
    private String fkUserId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("postedBy")
    @Expose
    private String postedBy;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("audio_url")
    @Expose
    private String audioUrl;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("approval")
    @Expose
    private Integer approval;
    @SerializedName("approvename")
    @Expose
    private String approvename;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("eventcategoryName")
    @Expose
    private String eventcategoryName;
    @SerializedName("festival_eventcategory")
    @Expose
    private String festival_eventcategory;

    @SerializedName("current")
    @Expose
    private String current;

    public String getFestival_eventcategory() {
        return festival_eventcategory;
    }

    public void setFestival_eventcategory(String festival_eventcategory) {
        this.festival_eventcategory = festival_eventcategory;
    }

    public Integer getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(Integer festivalId) {
        this.festivalId = festivalId;
    }

    public String getNameOffestival() {
        return nameOffestival;
    }

    public void setNameOffestival(String nameOffestival) {
        this.nameOffestival = nameOffestival;
    }

    public String getFestivalUrl() {
        return festivalUrl;
    }

    public void setFestivalUrl(String festivalUrl) {
        this.festivalUrl = festivalUrl;
    }

    public String getFestivalSdate() {
        return festivalSdate;
    }

    public void setFestivalSdate(String festivalSdate) {
        this.festivalSdate = festivalSdate;
    }

    public String getFestivalEdate() {
        return festivalEdate;
    }

    public void setFestivalEdate(String festivalEdate) {
        this.festivalEdate = festivalEdate;
    }

    public String getFestivalDescription() {
        return festivalDescription;
    }

    public void setFestivalDescription(String festivalDescription) {
        this.festivalDescription = festivalDescription;
    }

    public String getFestivalCountry() {
        return festivalCountry;
    }

    public void setFestivalCountry(String festivalCountry) {
        this.festivalCountry = festivalCountry;
    }

    public String getFestivalState() {
        return festivalState;
    }

    public void setFestivalState(String festivalState) {
        this.festivalState = festivalState;
    }

    public String getFestivalCity() {
        return festivalCity;
    }

    public void setFestivalCity(String festivalCity) {
        this.festivalCity = festivalCity;
    }

    public String getFestivalDistrict() {
        return festivalDistrict;
    }

    public void setFestivalDistrict(String festivalDistrict) {
        this.festivalDistrict = festivalDistrict;
    }

    public String getFestivalLocation() {
        return festivalLocation;
    }

    public void setFestivalLocation(String festivalLocation) {
        this.festivalLocation = festivalLocation;
    }

    public String getFestivalZipcode() {
        return festivalZipcode;
    }

    public void setFestivalZipcode(String festivalZipcode) {
        this.festivalZipcode = festivalZipcode;
    }

    public String getFestivalContactname() {
        return festivalContactname;
    }

    public void setFestivalContactname(String festivalContactname) {
        this.festivalContactname = festivalContactname;
    }

    public String getFestivalContactmobile() {
        return festivalContactmobile;
    }

    public void setFestivalContactmobile(String festivalContactmobile) {
        this.festivalContactmobile = festivalContactmobile;
    }

    public String getFestivalContactemail() {
        return festivalContactemail;
    }

    public void setFestivalContactemail(String festivalContactemail) {
        this.festivalContactemail = festivalContactemail;
    }

    public String getFestivalContactaddress() {
        return festivalContactaddress;
    }

    public void setFestivalContactaddress(String festivalContactaddress) {
        this.festivalContactaddress = festivalContactaddress;
    }

    public String getFestivalImage() {
        return festivalImage;
    }

    public void setFestivalImage(String festivalImage) {
        this.festivalImage = festivalImage;
    }

    public String getFestivalOrder() {
        return festivalOrder;
    }

    public void setFestivalOrder(String festivalOrder) {
        this.festivalOrder = festivalOrder;
    }

    public Integer getFestivalCategory() {
        return festivalCategory;
    }

    public void setFestivalCategory(Integer festivalCategory) {
        this.festivalCategory = festivalCategory;
    }

    public Integer getFkTypeId() {
        return fkTypeId;
    }

    public void setFkTypeId(Integer fkTypeId) {
        this.fkTypeId = fkTypeId;
    }

    public String getFestivalSeotitle() {
        return festivalSeotitle;
    }

    public void setFestivalSeotitle(String festivalSeotitle) {
        this.festivalSeotitle = festivalSeotitle;
    }

    public String getFestivalSeodes() {
        return festivalSeodes;
    }

    public void setFestivalSeodes(String festivalSeodes) {
        this.festivalSeodes = festivalSeodes;
    }

    public String getFestivalSeokey() {
        return festivalSeokey;
    }

    public void setFestivalSeokey(String festivalSeokey) {
        this.festivalSeokey = festivalSeokey;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getApproval() {
        return approval;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }

    public String getApprovename() {
        return approvename;
    }

    public void setApprovename(String approvename) {
        this.approvename = approvename;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEventcategoryName() {
        return eventcategoryName;
    }

    public void setEventcategoryName(String eventcategoryName) {
        this.eventcategoryName = eventcategoryName;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}