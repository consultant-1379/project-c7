package com.ericsson.graduates.team2.dashboard.controller;

import com.ericsson.graduates.team2.dashboard.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class JenkinsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private User userSingleton;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testIndexWithUserDetail() throws Exception {
        userSingleton.setUsername("ENMROSO");
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testIndexWithoutUserDetail() throws Exception {
        userSingleton.setUsername(null);
        this.mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("login"));
    }

}
