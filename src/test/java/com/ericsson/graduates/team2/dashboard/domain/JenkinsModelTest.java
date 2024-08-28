package com.ericsson.graduates.team2.dashboard.domain;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class JenkinsModelTest {

    JenkinsModel jenkinsModel;


    @BeforeEach
    void setup() {
        jenkinsModel = new JenkinsModel("test_job","YEAR",101,4,0,0,0);
    }


    @Test
    void testGettersAndSetters() {
        jenkinsModel = new JenkinsModel();
        jenkinsModel.setJobType("test_job");
        assertThat(jenkinsModel.getJobType(), is("test_job"));
        jenkinsModel.setFilterTime("YEAR");
        assertThat(jenkinsModel.getFilterTime(), is("YEAR"));
        jenkinsModel.setBuildNumber(101);
        assertThat(jenkinsModel.getBuildNumber(), is(101));
        jenkinsModel.setNumberOfDeliveries(4);
        assertThat(jenkinsModel.getNumberOfDeliveries(), is(4));
        jenkinsModel.setDuration(0);
        assertThat(jenkinsModel.getDuration(), is(0.0));
        jenkinsModel.setFailureRate(0);
        assertThat(jenkinsModel.getFailureRate(), is(0.0));
        jenkinsModel.setRestoreTime(0);
        assertThat(jenkinsModel.getRestoreTime(), is(0.0));
    }


    @Test
    void testConstructor() {
        jenkinsModel = new JenkinsModel("test_job","YEAR",101,4,0,0,0);
        assertThat(jenkinsModel.getJobType(), is("test_job"));
        assertThat(jenkinsModel.getFilterTime(), is("YEAR"));
        assertThat(jenkinsModel.getBuildNumber(), is(101));
        assertThat(jenkinsModel.getNumberOfDeliveries(), is(4));
        assertThat(jenkinsModel.getDuration(), is(0.0));
        assertThat(jenkinsModel.getFailureRate(), is(0.0));
        assertThat(jenkinsModel.getRestoreTime(), is(0.0));
    }


    @Test
    void testToString() {
        String result = "JenkinsModel{" +
                "id=" + jenkinsModel.getId() +
                ", jobType='" + jenkinsModel.getJobType() + '\'' +
                ", filterTime='" + jenkinsModel.getFilterTime() + '\'' +
                ", buildNumber=" + jenkinsModel.getBuildNumber() +
                ", numberOfDeliveries=" + jenkinsModel.getNumberOfDeliveries() +
                ", duration=" + jenkinsModel.getDuration() +
                ", failureRate=" + jenkinsModel.getFailureRate() +
                ", restoreTime=" + jenkinsModel.getRestoreTime() +
                '}';

        assertThat(jenkinsModel.toString(), is(result));
    }



}
