package com.ericsson.graduates.team2.dashboard.service;

import com.ericsson.graduates.team2.dashboard.domain.FilterOption;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class JenkinsServiceImpl implements JenkinsService {

    private List<BuildWithDetails> allBuildWithDetailsList = new LinkedList<>();
    private List<BuildWithDetails> filteredList = new LinkedList<>();

    public void getFilteredList(Job job, Date date, FilterOption filterOption) throws IOException {
        if (allBuildWithDetailsList.isEmpty())
            getAllBuildsForJob(job);
        filteredList.clear();

        LocalDate localDateProvided = new Timestamp(date.getTime()).toLocalDateTime().toLocalDate();

        if (filterOption.name().equals("ALL"))
            filteredList = allBuildWithDetailsList;

        BuildWithDetails lastBuild = job.details().getLastBuild().details();
        LocalDate lastRun = new Timestamp(new Date(lastBuild.getTimestamp()).getTime()).toLocalDateTime().toLocalDate();

        if (checkDatesAreSamePeriod(localDateProvided, lastRun, filterOption)) {
            for (BuildWithDetails b : allBuildWithDetailsList) {
                LocalDate buildDate = new Timestamp(
                        new Date(b.getTimestamp()).getTime()).toLocalDateTime().toLocalDate();
                boolean samePeriod = checkDatesAreSamePeriod(localDateProvided, buildDate, filterOption);

                if (samePeriod)
                    filteredList.add(b);
            }

        }
    }

    public int getDeliveryCountForFilter()  {
        return filteredList.size();
    }

    private boolean checkDatesAreSamePeriod(LocalDate dateOne, LocalDate dateTwo, FilterOption filterOption) {
        Integer weekOne = getWeekFromDate(dateOne);
        Integer weekTwo = getWeekFromDate(dateTwo);

        if (dateOne.getYear() == dateTwo.getYear()) {
            if (filterOption.name().equals("YEAR")) {
                return true;
            } else if (dateOne.getMonthValue() == dateTwo.getMonthValue()) {
                if (filterOption.name().equals("MONTH")) {
                    return true;
                } else if (weekOne == weekTwo) {
                    if (filterOption.name().equals("WEEK")) {
                        return true;
                    } else if (dateOne.getDayOfMonth() == dateTwo.getDayOfMonth()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public double getRestoreTime(Job job) throws IOException {
        Build lastFailedBuild = job.details().getLastFailedBuild();
        if(lastFailedBuild.getNumber() == job.details().getLastBuild().getNumber()){
            return 0;
        }

        LocalDateTime failedBuildStamp = new Timestamp(lastFailedBuild.details().getTimestamp()).toLocalDateTime();
        int failedBuildIndex = lastFailedBuild.getNumber();
        Build nextSuccessfulBuild = lastFailedBuild;
        int currBuildIndex = failedBuildIndex+1;
        while (nextSuccessfulBuild.details().getResult().name().equals("FAILURE")) {
            nextSuccessfulBuild = job.details().getBuildByNumber(currBuildIndex);
            currBuildIndex++;
        }
        LocalDateTime successBuildStamp = new Timestamp(nextSuccessfulBuild.details().getTimestamp()).toLocalDateTime();
        return (Duration.between(failedBuildStamp,successBuildStamp).toMillis() / 1000.0);
    }

    public double getFailureRate() {
        int size = filteredList.size();
        if (size == 0)
            return 0;
        long failCount = getFailureBuildCount(filteredList);
        double num = failCount;
        double sizeVal = size;
        double rate = num / sizeVal;
        return rate * 100.0;
    }

    private Integer getWeekFromDate(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private static boolean isSameYear(LocalDate date1, LocalDate date2) {
        return date2.isAfter(date1.minusYears(1));
    }

    private static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date2.isAfter(date1.minusMonths(1));
    }

    private static boolean isSameWeek(LocalDate date1, LocalDate date2) {
        return date2.isAfter(date1.minusWeeks(1));
    }

    private static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.getDayOfMonth() == date2.getDayOfMonth();
    }

    private long getFailureBuildCount(List<BuildWithDetails> builds) {
        long count =  builds.stream().filter(b -> b.getResult().name().equals("FAILURE")).count();
        return count;
    }


    private boolean isFailure(BuildWithDetails buildWithDetails) throws IOException {
        return buildWithDetails.details().getResult().name().equalsIgnoreCase("FAILURE");
    }


    private void getAllBuildsForJob(Job job) throws IOException {
        for (Build build : job.details().getAllBuilds())
            allBuildWithDetailsList.add(build.details());
    }

    public void clearBuilds() {
        allBuildWithDetailsList.clear();
        filteredList.clear();
    }
}