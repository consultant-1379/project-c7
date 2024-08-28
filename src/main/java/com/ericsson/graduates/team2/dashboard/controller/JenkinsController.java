package com.ericsson.graduates.team2.dashboard.controller;

import com.ericsson.graduates.team2.dashboard.domain.JenkinsModel;
import com.ericsson.graduates.team2.dashboard.domain.JenkinsModelDAO;
import com.ericsson.graduates.team2.dashboard.domain.User;
import com.ericsson.graduates.team2.dashboard.service.JenkinsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@CrossOrigin
public class JenkinsController {

    @Autowired
    private User userSingleton;

    @Autowired
    private JenkinsModelDAO jenkinsModelDAO;

    private JenkinsServiceImpl service = new JenkinsServiceImpl();

    @Value("${jenkins.client.api}")
    private String jenkinsUrl;

    @GetMapping(value = "")
    public String index(@RequestParam(defaultValue="DAY") String period,
                        RedirectAttributes redirectAttributes, Model model) {

        if (userSingleton.getUsername() == null) {
            redirectAttributes.addFlashAttribute("user", new User());
            return "redirect:login";
        }

        List<JenkinsModel> jobs = jenkinsModelDAO.findByFilterTime(period);

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobsSize", jobs.size());

        return "index";
    }

}
