package org.camunda.bpm.extension.jobexecutor;

import org.apache.commons.lang3.time.DateUtils;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.cfg.MostUsefulProcessEngineConfiguration;
import org.camunda.bpm.extension.FluentTimerJobCreator;
import org.camunda.bpm.extension.RestRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class RestRequestJobHandlerTest {

    private final MostUsefulProcessEngineConfiguration cfg = MostUsefulProcessEngineConfiguration.mostUsefulProcessEngineConfiguration();

    {
        cfg.addCustomJobHandler(new RestRequestJobHandler());
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Rule
    public final ProcessEngineRule processEngine = new ProcessEngineRule(cfg.buildProcessEngine());

    private ManagementService managementService;

    @Before
    public void setUp() {
        managementService = processEngine.getManagementService();
    }

    @Test
    public void create_with_fluent() {

        final RestRequest post = RestRequest.post("http://localhost:8080/engine-rest/task/70b8815b-4289-11e4-b4be-b8e85641c4bc/unclaim");

        logger.info("post: {}", post.toJSon());

        // @formatter:off
        String id = new FluentTimerJobCreator(processEngine.getProcessEngine())
                .duedate(DateUtils.addDays(new Date(), 1))
                .jobHandlerType(RestRequestJobHandler.TYPE)
                .jobHandlerConfiguration(post.toJSon())
                .createJob();
        // @formatter:on

        assertNotNull(findJob());

        logger.info(id);

        managementService.executeJob(findJob().getId());


    }

    private Job findJob() {
        return managementService.createJobQuery().singleResult();
    }

}