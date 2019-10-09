package domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Map<String, String> headerMap;

    private String url;

    private String httpProtocolVersion;

    private Date requestDate;

    private String httpMethod;

    private String content;

    public HttpRequest() {
        requestDate = new Date();
        headerMap = new HashMap<String, String>();
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void addHeader(String key, String value) {
        headerMap.put(key,value);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHttpProtocolVersion() {
        return httpProtocolVersion;
    }

    public void setHttpProtocolVersion(String httpProtocolVersion) {
        this.httpProtocolVersion = httpProtocolVersion;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "headerMap=" + headerMap +
                ", url='" + url + '\'' +
                ", httpProtocolVersion='" + httpProtocolVersion + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
