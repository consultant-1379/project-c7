package com.ericsson.graduates.team2.dashboard.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface JenkinsModelDAO extends CrudRepository<JenkinsModel, String> {

    Optional<JenkinsModel> findById(String jobName);
    List<JenkinsModel> findByFilterTime(String filterTime);

}

