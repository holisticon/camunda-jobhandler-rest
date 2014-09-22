package org.camunda.bpm.extension;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class FluentTimerJobCreator  {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private transient final CommandExecutor commandExecutor;
    private String jobHandlerType;
    private String jobHandlerConfiguration;
    private Date duedate;

    public FluentTimerJobCreator(final ProcessEngine processEngine) {
        this(((ProcessEngineImpl) processEngine).getProcessEngineConfiguration().getCommandExecutorSchemaOperations());
    }

    public FluentTimerJobCreator(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public FluentTimerJobCreator jobHandlerType(final String jobHandlerType) {
        this.jobHandlerType = jobHandlerType;
        return this;
    }

    public FluentTimerJobCreator jobHandlerConfiguration(final String jobHandlerConfiguration) {
        this.jobHandlerConfiguration = jobHandlerConfiguration;
        return this;
    }

    public FluentTimerJobCreator duedate(final Date duedate) {
        this.duedate = duedate;
        return this;
    }

    public String createJob() {
        return commandExecutor.execute(new Command<String>() {
            @Override
            public String execute(final CommandContext _) {
                final TimerEntity timer = new TimerEntity();

                timer.setDuedate(duedate);
                timer.setJobHandlerConfiguration(jobHandlerConfiguration);
                timer.setJobHandlerType(jobHandlerType);

                logger.info("timer: {}", timer );

                Context.getCommandContext().getJobManager().schedule(timer);

                return timer.getId();
            }
        });
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, true);
    }
}
