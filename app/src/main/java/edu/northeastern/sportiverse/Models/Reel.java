package edu.northeastern.sportiverse.Models;

public class Reel {
    private String reelUrl = "";
    private String caption = "";
    private String profileLink;

    // Default constructor required for Firebase
    public Reel() {
    }

    // Constructor for Reel without profile link
    public Reel(String reelUrl, String caption) {
        this.reelUrl = reelUrl;
        this.caption = caption;
    }

    // Constructor for Reel with profile link
    public Reel(String reelUrl, String caption, String profileLink) {
        this.reelUrl = reelUrl;
        this.caption = caption;
        this.profileLink = profileLink;
    }

    // Getters and Setters
    public String getReelUrl() {
        return reelUrl;
    }

    public void setReelUrl(String reelUrl) {
        this.reelUrl = reelUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }
}
