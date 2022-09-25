// A Java program for a Server
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Base64;

import javax.imageio.ImageIO;

import model.*;


public class Server
{
	private ServerSocket server = null;
	private DataInputStream in = null;

	public Server(int port)
	{
		try{
			server = new ServerSocket(port);
			System.out.println("Listening to clients of port "+port);
		}
		catch(IOException ex){
			System.out.println("Unable to create server.");
			System.exit(1);
		}

		int i = 0;
		while(true){
			try{
				i++;
				Socket socket = server.accept(); //waiting for a new client connection
				Thread t = new Thread(new ServerHandler(socket, i));
				t.start(); //letting a new thread serve the client
			}
			catch(IOException ex){
				System.out.println("Unable to accept socket connection.");
			}
		}
	}

	public static void main(String args[])
	{
		Server server = new Server(80);
	}

	private class ServerHandler implements Runnable {
		private Socket socket = null;
		private int messageNumber = 0;
		private String requestedFilePath = "hdjkhske.txt";
		private HttpRequest httpRequest = null;

		public ServerHandler(Socket socket, int messageNumber){
			System.out.println("Passed messageNumber: "+messageNumber);
			this.socket = socket;
			this.messageNumber = messageNumber;
		}
		public void run(){
			try{
				readInput();
				writeToStream();
				socket.close();
			}
			catch(IOException ex)
			{
				System.out.println("IOException: "+ ex);
			}
		}

		/*
		private void writeToStream1() throws IOException{
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			PrintWriter sOut = new PrintWriter(socket.getOutputStream(), true);
			File file = new File(requestedFilePath);
			FileInputStream fileStream = new FileInputStream(file);
			File targetFile = new File("out.txt");
			OutputStream outStream = new FileOutputStream(targetFile);
			HttpResponse response = new HttpResponse();

			//out.write(response.buildMetadata(new StringBuilder()).getBytes());
			outStream.write(response.buildMetadata(new StringBuilder()).getBytes());

			int count;
			byte[] buffer = new byte[4*1024];
			out.writeLong(file.length());
			while ((count = fileStream.read(buffer)) > 0)
			{
				out.write(buffer, 0, count);
				out.flush();
				outStream.write(buffer, 0, count);
			}

			//out.flush();
			//out.close();
			outStream.close();
			fileStream.close();
		}
		*/

		private void readInput() throws IOException {
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
			
			/*
			System.out.println("Method: "+httpRequest.getHttpMethod());
			System.out.println("URI: "+httpRequest.getUri());
			System.out.println("Version: "+httpRequest.getVersion());
			*/
		}

		private void assignRequestedPath(HttpRequest httpRequest) {
			if(httpRequest.getUri().equals("/")) {
				this.requestedFilePath = "/index.html";
			}
			else {
				this.requestedFilePath = httpRequest.getUri();
			}
		}
		private void writeToStream() throws IOException{
			PrintWriter sOut = new PrintWriter(socket.getOutputStream(), true);
			HttpResponse response = new HttpResponse(getFileContents(), requestedFilePath);
			//System.out.println("Response:::: "+response.buildResponse());
			sOut.println(response.buildResponse());
			//sOut.println(response.buildMetadata(new StringBuilder()));
			//System.out.println("Response meta:::: "+response.buildMetadata(new StringBuilder()));
			sOut.flush();
			//writeFileContents(socket.getOutputStream());
		}

		private String getFileContents() throws IOException {
			try {
				System.out.println("User Dir: "+System.getProperty("user.dir"));
				System.out.println("Requested path: "+requestedFilePath);
				FileInputStream fileStream = new FileInputStream("C:\\SJSU\\Assignments\\CS 249 - Distributed Computing\\3\\Proj" + requestedFilePath);

				StringBuilder resultStringBuilder = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
				String line;
				while ((line = br.readLine()) != null) {
					resultStringBuilder.append(line).append("\n");
				}

				return resultStringBuilder.toString();
			} catch(FileNotFoundException ex) {
				System.out.println("File not found for Requested path: "+requestedFilePath);
				return "The content of this file was not found.";
			}
		}

		private void writeFileContents(OutputStream out) throws IOException {
			try {
				if(requestedFilePath.endsWith(".png") 
					|| requestedFilePath.endsWith(".jpg") 
					|| requestedFilePath.endsWith(".jpeg")
					|| requestedFilePath.endsWith(".gif")){
					File file = new File(requestedFilePath);
					BufferedImage imageBuffer = ImageIO.read(file);
					ImageIO.write(imageBuffer, "jpeg", out);
				}
				else {
					FileInputStream fileStream = new FileInputStream(System.getProperty("user.dir") + requestedFilePath);

					StringBuilder resultStringBuilder = new StringBuilder();
					BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
					String line;
					while ((line = br.readLine()) != null) {
						resultStringBuilder.append(line).append("\n");
					}

					PrintWriter printWriter = new PrintWriter(out, true);
					printWriter.println(resultStringBuilder.toString());
					printWriter.flush();
				}
			} catch(FileNotFoundException ex) {
				System.out.println("File not found for Requested path: "+requestedFilePath);
			}
		}
		
	}
}
