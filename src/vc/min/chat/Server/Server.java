package vc.min.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple chat server based on the protocol listed in README.md
 * 
 * @author Ryan Shaw
 *
 */

public class Server implements Runnable{
		
	private boolean running;
	
	/**
	 * The server port
	 */
	private int port;
	
	/**
	 * Maximum number of connections to allow
	 */
	private int maxConnections;
	
	/**
	 * The server socket
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Convenience constructor
	 * 
	 * @param port
	 * @param maxConnections
	 */
	public Server(int port, int maxConnections){
		this.port = port;
		this.maxConnections = maxConnections;
		this.running = false;
	}
	
	public void stopServer() throws IOException{
		running = false;
		serverSocket.close();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			System.err.println("Failed to start: " + e.getMessage());
			return;
		}
		while(running){
			try {
				Socket clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("Failed to accept client: " + e.getMessage());
			}
		}
	}

}
