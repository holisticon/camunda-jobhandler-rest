package org.camunda.bpm.engine.test.cfg;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;

import java.util.ArrayList;

/**
 * Configuration that makes the standard camunda.cfg.xml obsolete by setting the
 * history, schema and job-executor settings.
 */
public class MostUsefulProcessEngineConfiguration extends StandaloneInMemProcessEngineConfiguration {

    public static MostUsefulProcessEngineConfiguration mostUsefulProcessEngineConfiguration() {
        return new MostUsefulProcessEngineConfiguration();
    }

    public MostUsefulProcessEngineConfiguration() {
        this.history = "full";
        this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
        this.jobExecutorActivate = false;
        this.expressionManager = new MockExpressionManager();
        this.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
        this.setCustomJobHandlers(new ArrayList<JobHandler>());
    }

    public void addCustomJobHandler(final JobHandler jobHandler) {
        checkArgument(jobHandler != null, "jobHandler must not be null!");
        getCustomJobHandlers().add(jobHandler);
    }

    public void addCustomPostBpmnParseListener(final BpmnParseListener bpmnParseListener) {
        checkArgument(bpmnParseListener != null, "bpmnParseListener must not be null!");
        getCustomPostBPMNParseListeners().add(bpmnParseListener);
    }

    private static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
