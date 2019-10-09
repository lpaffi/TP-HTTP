package domain;

import javax.ws.rs.core.Response.Status;

public class HttpResponse {

    private String httpProtocolVersion;

    private Status httpStatus;

    private String contentType;

    private String serverName;

    private String content;

    public HttpResponse() {
        httpStatus = Status.OK;
    }

    public String getHttpProtocolVersion() {
        return httpProtocolVersion;
    }

    public void setHttpProtocolVersion(String httpProtocolVersion) {
        this.httpProtocolVersion = httpProtocolVersion;
    }

    public Status getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Status httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return httpProtocolVersion + " " + httpStatus.getStatusCode() + " " + httpStatus + "\n"
                + contentType + " \n" + serverName + "\n"
                + "\n"
                + content;
    }
}
