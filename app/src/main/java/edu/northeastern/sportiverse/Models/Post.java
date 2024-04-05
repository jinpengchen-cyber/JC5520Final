package edu.northeastern.sportiverse.Models;

public class Post {

    private String id;
    private String title;
    private String description;
    private String image;
    private String userId;
    private String userName;
    private String userImage;
    private String date;

    private long time;
    private Double latitude;
    private Double longitude;

    public Post() {
    }

    public Post(String id, String title, String description, String image, String userId, String userName, String userImage, long time, Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Post(String image, String description, String userId, long time, Double latitude, Double longitude) {
        this.image = image;
        this.description = description;
        this.userId = userId;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostUrl() {
        return image;
    }

    public void setPostUrl(String image) {
        this.image = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    // The adapter is using getUid(), so ensure there's a corresponding getter named getUid
    public String getUid() {
        return userId;
    }

    public void setUid(String userId) {
        this.userId = userId;
    }

    // The description is used as the caption in the adapter
    public String getCaption() {
        return description;
    }

    public void setCaption(String description) {
        this.description = description;
    }

    // Update your getter and setter methods to use 'Double'
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

