package com.ericsson.graduates.team2.dashboard.domain;

import javax.persistence.*;

//Data to be sent to the front end, calculated as per specifications
@Entity
@Table(name="job")
public class JenkinsModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "job_type")
    private String jobType;

    @Column(nullable = false, name = "filter_time")
    private String filterTime;

    @Column(nullable = false, name = "build_number")
    private int buildNumber;

    @Column(nullable = false, name = "number_of_deliveries")
    private int numberOfDeliveries;

    @Column(nullable = false, name = "duration")
    private double duration;

    @Column(nullable = false, name = "failure_rate")
    private double failureRate;
    @Column(nullable = false, name = "restore_time")
    private double restoreTime;

    public JenkinsModel(){}

    public JenkinsModel(String jobType, String filterTime, int buildNumber, int numberOfDeliveries, double duration, double failureRate, double restoreTime) {
        this.jobType = jobType;
        this.filterTime = filterTime;
        this.buildNumber = buildNumber;
        this.numberOfDeliveries = numberOfDeliveries;
        this.duration = duration;
        this.failureRate = failureRate;
        this.restoreTime = restoreTime;
    }

    public int getId() {
        return id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getFilterTime() {
        return filterTime;
    }

    public void setFilterTime(String filterTime) {
        this.filterTime = filterTime;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public int getNumberOfDeliveries() {
        return numberOfDeliveries;
    }

    public void setNumberOfDeliveries(int numberOfDeliveries) {
        this.numberOfDeliveries = numberOfDeliveries;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(float failureRate) {
        this.failureRate = failureRate;
    }

    public double getRestoreTime() {
        return restoreTime;
    }

    public void setRestoreTime(float restoreTime) {
        this.restoreTime = restoreTime;
    }

    @Override
    public String toString() {
        return "JenkinsModel{" +
                "id=" + id +
                ", jobType='" + jobType + '\'' +
                ", filterTime='" + filterTime + '\'' +
                ", buildNumber=" + buildNumber +
                ", numberOfDeliveries=" + numberOfDeliveries +
                ", duration=" + duration +
                ", failureRate=" + failureRate +
                ", restoreTime=" + restoreTime +
                '}';
    }
}