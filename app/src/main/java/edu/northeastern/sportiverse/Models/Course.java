package edu.northeastern.sportiverse.Models;

public class Course {

    private String courseName;
    private String courseDescription;
    private String videoUrl;
    private String courseID;

    public Course() {
    }

    public Course(String courseName, String courseDescription, String videoUrl, String courseID) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.videoUrl = videoUrl;
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }


}
