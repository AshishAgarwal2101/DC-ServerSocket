package model;

import java.util.*;

public class HttpRequest {
    private String httpMethod;
    private String uri;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();

    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String PUT_REQUEST = "PUT";

    public static void buildRequest(HttpRequest httpRequest, String requestStr){
        requestStr.replace("\r", "");
        String[] lines = requestStr.split("\n");
        //System.out.println("Request String::::::: "+requestStr);
        httpRequest.buildMetadata(lines);
        httpRequest.buildHeaders(lines);
    }

    private void buildMetadata(String[] requestLines){
        String[] strParts = requestLines[0].split(" ");
        this.httpMethod = strParts[0];
        this.uri = strParts[1];
        this.httpVersion = strParts[2];
    }

    private void buildHeaders(String[] requestLines){
        for(int i=1; i<requestLines.length; i++){
            String[] parts = requestLines[i].split(":");
            headers.put(parts[0].trim(), parts[1].trim());
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUri(){
        return uri;
    }
    
    public String getVersion() {
        return httpVersion;
    }
}
