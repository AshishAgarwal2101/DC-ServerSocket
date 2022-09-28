package model;

import java.util.*;

public class HttpResponse {
    private String httpVersion = "HTTP/1.0";
    private int httpStatusCode = 200;
    private String httpStatus = "OK";
    private Map<String, String> headers = new HashMap<>();
    private String body = "";

    public HttpResponse(){}

    public HttpResponse(String requestedFilePath){
        assignContentType(requestedFilePath);
    }

    public HttpResponse(String requestedFilePath, String body){
        this(requestedFilePath);
        this.body = body;
    }

    private void assignContentType(String requestedFilePath) {
        if(requestedFilePath.endsWith(".html")) {
            headers.put("Content-Type", "text/html");
        }
        else if(requestedFilePath.endsWith(".txt")) {
            headers.put("Content-Type", "text/plain");
        }
        else if(requestedFilePath.endsWith(".jpg") || requestedFilePath.endsWith(".jpeg")) {
            headers.put("Content-Type", "image/jpg");
        }
        else if(requestedFilePath.endsWith(".jpeg")) {
            headers.put("Content-Type", "image/jpeg");
        }
        else if(requestedFilePath.endsWith(".png")) {
            headers.put("Content-Type", "image/png");
        }
        else if(requestedFilePath.endsWith(".gif")) {
            headers.put("Content-Type", "image/gif");
        }
        else if(requestedFilePath.endsWith(".svg")) {
            headers.put("Content-Type", "image/svg+xml");
        }
        else if(requestedFilePath.endsWith(".css") || requestedFilePath.endsWith("css")) {
            headers.put("Content-Type", "text/css");
        }
        else if(requestedFilePath.endsWith(".js") || requestedFilePath.endsWith(".jsx")) {
            headers.put("Content-Type", "text/javascript");
        }
    }

    public void appendOrReplaceHeaders(Map<String, String> callerHeaders){
        for(String headerKey:callerHeaders.keySet()){
            String headerValue = callerHeaders.get(headerKey);
            headers.put(headerKey, headerValue);
        }
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setHttpVersion(String httpVersion){
        this.httpVersion = httpVersion;
    }

    public void setHttpStatus(int httpStatusCode, String httpStatus){
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
    }

    public String buildResponse(){
        StringBuilder responseBuilder = new StringBuilder();

        buildMetadata(responseBuilder);
        buildBody(responseBuilder);

        return responseBuilder.toString();
    }

    public String buildMetadata(StringBuilder responseBuilder){
        headers.put("Date", (new Date()).toInstant().toString());
        responseBuilder.append(httpVersion).append(" ").append(httpStatusCode).append(" ").append(httpStatus);
        for(String headerKey:headers.keySet()){
            String headerValue = headers.get(headerKey);
            responseBuilder.append("\r\n").append(headerKey).append(":").append(headerValue);
        }
        
        responseBuilder.append("\r\n\r\n");
        return responseBuilder.toString();
    }

    public String buildMetadata(StringBuilder responseBuilder, long fileContentLength){
        headers.put("Content-Length", fileContentLength + "");
        return buildMetadata(responseBuilder);
    }

    public String buildBody(StringBuilder responseBuilder){
        responseBuilder.append(body);
        return responseBuilder.toString();
    }
}
