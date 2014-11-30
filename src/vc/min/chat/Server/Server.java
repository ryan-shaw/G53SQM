package vc.min.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vc.min.chat.Shared.Packets.Login0Packet;
import vc.min.chat.Shared.Packets.Packet;

/**
 * Simple chat server based on the protocol listed in README.md
 * 
 * @author Ryan Shaw
 *
 */

public class Server implements Runnable{
	
	public static void main(String[] args){
		new Thread(new Server(0, 4)).start();
	}
		
	/**
	 * Thread running field
	 */
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
	 * Holds the client sockets
	 */
	private ArrayList<ClientSocket> clientSockets;
	
	/**
	 * Convenience constructor
	 * 
	 * @param port
	 * @param maxConnections
	 */
	public Server(int port, int maxConnections){
		this.port = port;
		this.maxConnections = maxConnections;
		this.running = true;
		clientSockets = new ArrayList<ClientSocket>();
	}

	/**
	 * Stops the server
	 * @throws IOException
	 */
	public void stopServer() throws IOException{
		running = false;
		for(ClientSocket client : clientSockets){
			client.close();
		}
		serverSocket.close();
	}
	
	/**
	 * Get the port the server is running on
	 * @return port
	 */
	public int getPort() {
		return serverSocket.getLocalPort();
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
				ClientSocket clientThread = new ClientSocket(clientSocket, this);
				clientSockets.add(clientThread);
				System.out.println("Added client");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to accept client: " + e.getMessage());
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
