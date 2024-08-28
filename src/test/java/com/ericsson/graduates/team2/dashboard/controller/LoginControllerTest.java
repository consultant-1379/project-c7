package com.ericsson.graduates.team2.dashboard.controller;

import com.ericsson.graduates.team2.dashboard.domain.FilterOption;
import com.ericsson.graduates.team2.dashboard.domain.JenkinsModel;
import com.ericsson.graduates.team2.dashboard.domain.JenkinsModelDAO;
import com.ericsson.graduates.team2.dashboard.domain.User;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
class LoginControllerTest {

    @Value("${jenkins.client.api}")
    private String jenkinsUrl;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private User userSingleton;

    @MockBean
    JenkinsModelDAO mockJenkinsModelDAO;

    private LoginController loginController;

    private MockMvc mockMvc;
    private Job job;
    private JenkinsServer jenkins;
    private static final String TEST_USERNAME = "emdansi";
    private static final String TEST_PASSWORD = "113715a4a81314ba2075d3fbddf3ca3059";

    private static final Logger LOGGER = Logger.getLogger(LoginControllerTest.class.getName());

    @BeforeEach
    void setup() throws URISyntaxException {
        loginController = new LoginController();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        setupJenkins();
        setupJob();
        setupJenkinsModels();
    }

    void setupJenkins() throws URISyntaxException {
        jenkins = new JenkinsServer(new URI(jenkinsUrl), TEST_USERNAME, TEST_PASSWORD);
    }

    void setupJob() {
        try {
            Map<String, Job> jenkinsJobs = jenkins.getJobs();
            for(Map.Entry<String, Job> jobEntry : jenkinsJobs.entrySet()) {
                job = jobEntry.getValue();
                break;
            }
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    void setupJenkinsModels() {
        List<JenkinsModel> jenkinsModels = new ArrayList<>();
        JenkinsModel jenkinsModel = new JenkinsModel(
                "", "", 0, 0, 0, 0, 0);
        jenkinsModels.add(jenkinsModel);
        when(mockJenkinsModelDAO.findAll()).thenReturn(jenkinsModels);
    }

    @Test
    void testGetLoginWithoutUserDetail() throws Exception {
        userSingleton.setUsername(null);
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    @Test
    void testGetLoginWithUserDetail() throws Exception {
        userSingleton.setUsername(TEST_USERNAME);
        userSingleton.setPassword(TEST_PASSWORD);
        mockMvc.perform(get("/login"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testPostLoginWithUserDetail() throws Exception {
        userSingleton.setUsername(TEST_USERNAME);
        userSingleton.setPassword(TEST_PASSWORD);

        mockMvc.perform(
                        post("/login")
                                .param("username", TEST_USERNAME)
                                .param("password", TEST_PASSWORD))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testPostLoginWithoutUserDetail() throws Exception {
        userSingleton.setUsername(null);
        userSingleton.setPassword(null);
        mockMvc.perform(post("/login"))
                .andExpect(redirectedUrl("login"));
    }

    @Test
    void testPostLogoutWithUserDetail() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(redirectedUrl("login"));
    }

    @Test
    void testSaveJenkinsModelsToDatabaseFailure(){
        Job job = new Job();
        Assertions.assertThrows(NullPointerException.class, () -> {
            List<JenkinsModel> jenkinsModels = loginController.saveJenkinsModelsToDatabase(job);
            assertThat(jenkinsModels, is(null));
        });
    }

    @Test()
    void testGenerateJenkinsModelFailure(){
        Job job = new Job();
        for (FilterOption filterOption : FilterOption.values()) {
            Assertions.assertThrows(NullPointerException.class, () -> {
                JenkinsModel jenkinsModel = loginController.generateJenkinsModel(job, filterOption);
                assertThat(jenkinsModel, is(null));
            });
        }
    }
}