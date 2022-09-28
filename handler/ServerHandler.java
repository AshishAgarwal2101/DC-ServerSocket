package handler;

import java.net.*;
import java.io.*;
import model.HttpResponse;
import model.HttpRequest;

public class ServerHandler implements Runnable {
    private Socket socket = null;
    private String requestedFilePath = "/index.html";
    private HttpRequest httpRequest = null;
    private String rootDir = "";

    public ServerHandler(String rootDir, Socket socket){
        this.rootDir = rootDir;
        this.socket = socket;
    }
    public void run() {
        try{
            readInput();
            writeToStream();
            socket.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException: "+ ex.getMessage());
        }
    }
    private void readInput() throws IOException, ProtocolException {
        StringBuilder inputBuilder = new StringBuilder();

        InputStreamReader isr =  new InputStreamReader(socket.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        
        String line = null;
        do {
            line = in.readLine();
            inputBuilder.append(line).append("\n");
        } while(line!=null && !line.isEmpty());

        httpRequest = new HttpRequest();
        HttpRequest.buildRequest(httpRequest, inputBuilder.toString());
        assignRequestedPath(httpRequest);
    }

    private void assignRequestedPath(HttpRequest httpRequest) {
        if(!httpRequest.getUri().equals("/")) {
            this.requestedFilePath = httpRequest.getUri();
        }
    }

    private void writeToStream() throws IOException {
        PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        try {
            HttpResponse response = new HttpResponse(requestedFilePath);
            out.print(response.buildMetadata(new StringBuilder()));
            writeFileContents(out);
            out.close();
        } catch(FileNotFoundException ex) {
            HttpResponse response = new HttpResponse(requestedFilePath, "File not found.");
            response.setHttpStatus(404, "Not Found");
            out.print(response.buildResponse());
        } catch(SecurityException ex) {
            HttpResponse response = new HttpResponse(requestedFilePath, "Permission Denied.");
            response.setHttpStatus(403, "Forbidden");
            out.print(response.buildResponse());
        }  catch(Exception ex) {
            HttpResponse response = new HttpResponse(requestedFilePath, ex.getMessage());
            response.setHttpStatus(500, "Internal Server Error");
            out.print(response.buildResponse());
        }
    }

    private void writeFileContents(PrintStream out) throws IOException, FileNotFoundException {
        File file = new File(rootDir + requestedFilePath);
        if(!file.canRead()){
            throw new SecurityException();
        }

        FileInputStream fileStream = new FileInputStream(file);
        byte[] buffer = new byte[4*1024];
        int count;
        while ((count = fileStream.read(buffer)) > 0)
        {
            out.write(buffer, 0, count);
        }
    }
    
}