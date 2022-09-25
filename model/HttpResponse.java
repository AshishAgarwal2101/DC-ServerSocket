package model;

import java.util.*;

public class HttpResponse {
    private String httpVersion = "HTTP/1.1";
    private int httpStatusCode = 200;
    private String httpStatus = "OK";
    private Map<String, String> headers = new HashMap<>();
    private String body = "";

    public HttpResponse(){
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "Keep-Alive");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Content-Encoding", "gzip");
    }

    public HttpResponse(String body, String requestedFilePath){
        this();
        setBody(body);
        assignContentType(requestedFilePath);
    }

    private void assignContentType(String requestedFilePath) {
        if(requestedFilePath.endsWith(".html")) {
            headers.put("Content-Type", "text/html");
        }
        else if(requestedFilePath.endsWith(".jpg")) {
            headers.put("Content-Type", "image/jpeg");
        }
        else if(requestedFilePath.endsWith(".png")) {
            headers.put("Content-Type", "image/png");
        }
        else if(requestedFilePath.endsWith(".gif")) {
            headers.put("Content-Type", "image/gif");
        }
        else if(requestedFilePath.endsWith(".css")) {
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
        headers.put("Content-Length", String.valueOf(body.getBytes().length));
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
        responseBuilder.append(httpVersion).append(" ").append(httpStatusCode).append(" ").append(httpStatus).append("\r\n");
        for(String headerKey:headers.keySet()){
            String headerValue = headers.get(headerKey);
            responseBuilder.append(headerKey).append(":").append(headerValue).append("\r\n");
        }

        responseBuilder.append("\r\n");
        return responseBuilder.toString();
    }

    public String buildBody(StringBuilder responseBuilder){
        responseBuilder.append(body);
        return responseBuilder.toString();
    }
}
