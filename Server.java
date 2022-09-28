import java.net.*;
import java.io.*;
import java.util.*;
import handler.ServerHandler;


public class Server
{
	private ServerSocket server = null;

	public Server(String rootDir, int port)
	{
		try{
			server = new ServerSocket(port);
			System.out.println("Listening to clients of port "+port);
		}
		catch(IOException ex){
			System.out.println("Unable to create server.");
			System.exit(1);
		}

		while(true){
			try{
				Socket socket = server.accept(); //waiting for a new client connection
				Thread t = new Thread(new ServerHandler(rootDir, socket));
				t.start(); //letting a new thread serve the client
			}
			catch(IOException ex){
				System.out.println("Unable to accept socket connection.");
			}
		}
	}

	public static void main(String args[])
	{
		if(args.length < 2){
			System.out.print("Not enough arguments provided. Exiting.");
			System.exit(1);
		}
		String rootDir = args[0];
		int port = Integer.parseInt(args[1]);
		new Server(rootDir, port);
	}
}
