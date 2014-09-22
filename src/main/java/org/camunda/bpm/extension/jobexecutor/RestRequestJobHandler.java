package org.camunda.bpm.extension.jobexecutor;

import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.extension.RestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class RestRequestJobHandler implements JobHandler, Serializable {

    public static final String TYPE = "restRequestJobHandler";
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void execute(final String configuration, final ExecutionEntity execution, final CommandContext commandContext) {

        final RestRequest restRequest = new GsonBuilder().create().fromJson(configuration, RestRequest.class);

        final WebResource resource = webResource(restRequest);

        final String requestEntity = restRequest.getContent();
        logger.info(requestEntity);

        switch (restRequest.getRequestType()) {
            case PUT:
                resource.put(requestEntity);
                break;
            case POST:
                post(resource, requestEntity);
                break;
            case GET:
                throw new UnsupportedOperationException();
            case DELETE:
                resource.delete(requestEntity);
                break;
        }
    }

    private void post(final WebResource resource, final String requestEntity) {
        if (requestEntity != null) {
            resource.post(requestEntity);
        } else {
            resource.post();
        }
    }

    private WebResource webResource(RestRequest restRequest) {
        final Client client = Client.create(new DefaultClientConfig());
        final WebResource resource = client.resource(restRequest.getRequestUrl());
        resource.type(restRequest.getMediaType());
        resource.accept(restRequest.getMediaType());
        return resource;
    }
}
