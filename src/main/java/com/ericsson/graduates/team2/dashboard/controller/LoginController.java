package com.ericsson.graduates.team2.dashboard.controller;

import com.ericsson.graduates.team2.dashboard.domain.FilterOption;
import com.ericsson.graduates.team2.dashboard.domain.JenkinsModel;
import com.ericsson.graduates.team2.dashboard.domain.JenkinsModelDAO;
import com.ericsson.graduates.team2.dashboard.domain.User;
import com.ericsson.graduates.team2.dashboard.service.JenkinsServiceImpl;
import com.offbytwo.jenkins.JenkinsServer;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final Integer JOB_LOAD_LIMIT = 1;

    @Value("${jenkins.client.api}")
    private String jenkinsUrl;

    @Autowired
    private JenkinsModelDAO jenkinsModelDAO;

    @Autowired
    private User userSingleton;

    private JenkinsServiceImpl service = new JenkinsServiceImpl();

    private JenkinsServer jenkins;

    @GetMapping("/login")
    public String login(User user){
        if(userSingleton.getUsername() != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, RedirectAttributes redirectAttributes) throws URISyntaxException, IOException {

        URI url = new URI(jenkinsUrl);
        jenkins = new JenkinsServer( url, user.getUsername(), user.getPassword());

        if (jenkins.isRunning()) {
            userSingleton.setUsername(user.getUsername());
            userSingleton.setPassword(user.getPassword());

            loadJenkinsData();
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("message", "Username or Password are incorrect.");
            return "redirect:login";
        }
    }

    @PostMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes){
        userSingleton.setUsername(null);
        userSingleton.setPassword(null);
        redirectAttributes.addFlashAttribute("user", new User());
        return "redirect:login";
    }

    public int loadJenkinsData() throws IOException {

        List<JenkinsModel> jobs = (List<JenkinsModel>) jenkinsModelDAO.findAll();
        int jobsCount = 0;

        if(!jobs.isEmpty()) return jobsCount;

        Map<String, Job> jenkinsJobs = jenkins.getJobs();
        for(Map.Entry<String, Job> jobEntry : jenkinsJobs.entrySet()) {
            Job job = jobEntry.getValue();
            List<JenkinsModel> jenkinsModels = saveJenkinsModelsToDatabase(job);
            if (!jenkinsModels.isEmpty()) jobsCount++;
            if (jobsCount >= JOB_LOAD_LIMIT) break;
        }

        return jobsCount;
    }

    public List<JenkinsModel> saveJenkinsModelsToDatabase(Job job) throws IOException {
        List<JenkinsModel> jenkinsModels = new ArrayList<>();
        for (FilterOption filterOption : FilterOption.values()) {
            JenkinsModel jenkinsModel = generateJenkinsModel(job, filterOption);
            jenkinsModelDAO.save(jenkinsModel);
            jenkinsModels.add(jenkinsModel);
        }
        return jenkinsModels;
    }

    public JenkinsModel generateJenkinsModel(Job job, FilterOption filterOption) throws IOException {

        JenkinsModel jenkinsModel = new JenkinsModel();
        JobWithDetails jobDetail = job.details();

        Date date = new Date();
        service.getFilteredList(job,date,filterOption);

        jenkinsModel.setJobType(job.getName());
        jenkinsModel.setFilterTime(filterOption.name());
        jenkinsModel.setBuildNumber(jobDetail.getNextBuildNumber());
        jenkinsModel.setDuration(jobDetail.getLastBuild().details().getDuration() / 1000.0);
        jenkinsModel.setNumberOfDeliveries(service.getDeliveryCountForFilter());
        jenkinsModel.setFailureRate((float) service.getFailureRate());
        jenkinsModel.setRestoreTime((float) service.getRestoreTime(job));

        service.clearBuilds();

        return jenkinsModel;
    }
}

