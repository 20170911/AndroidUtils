package com.yuxliu.androidutils.bean;

/**
 * Created by gsd on 2021/6/3.
 */
public class TimerArg {

    public String lectureId;
    public int displayTime;
    public String timeType = "";
    public String timeStatus = "";
    public String role = "";

    public TimerArg() {
    }

    public TimerArg(String lectureId, int displayTime, String timeType, String timeStatus) {
        this.lectureId = lectureId;
        this.displayTime = displayTime;
        this.timeType = timeType;
        this.timeStatus = timeStatus;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
