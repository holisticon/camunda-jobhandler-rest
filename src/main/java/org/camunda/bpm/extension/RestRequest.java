package org.camunda.bpm.extension;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

public class RestRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public static RestRequest post(String requestUrl) {
        return post(requestUrl,null);
    }

    public static RestRequest post(String requestUrl, String content) {
        final RestRequest restRequest = new RestRequest(RequestType.POST);
        restRequest.setRequestUrl(requestUrl);
        restRequest.setContent(content);

        return restRequest;
    }



    public static enum RequestType {
        PUT, POST, GET, DELETE
    }

    private String requestUrl;
    private RequestType requestType;
    private String content;

    public RestRequest() {
        this(null);
    }

    public RestRequest(RequestType requestType) {
        this.requestType = requestType;
    }

    public MediaType getMediaType() {
        return MediaType.APPLICATION_JSON_TYPE;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    public String toJSon() {
        return new GsonBuilder().create().toJson(this);
    }
}
