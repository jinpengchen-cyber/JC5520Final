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

    public Post() {
    }

    public Post(String id, String title, String description, String image, String userId, String userName, String userImage, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.date = date;
    }

    public Post(String image, String description, String userId, String time) {
        this.image = image;
        this.description = description;
        this.userId = userId;
        this.date = time; // Assuming 'date' field should hold the time value. Adjust as needed.
        // Initialize other fields as necessary, or leave them to be set via setters.
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
}

