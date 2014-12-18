package vc.min.chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Simple chat server based on the protocol listed in README.md
 * 
 * @author Ryan Shaw
 *
 */
public class Server extends Thread implements IServer {
	
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
	private ArrayList<IClientSocket> clientSockets;

	private boolean accepting;
	
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
		clientSockets = new ArrayList<IClientSocket>();
		/* Thread to check clients are alive */
		new PingChecker(this).start();
	}

	public void stopServer() throws IOException{
		System.out.println("Shutting down...");
		running = false;
		for(IClientSocket client : clientSockets){
			client.close("server stopping");
		}
		serverSocket.close();
	}
	
	public ArrayList<IClientSocket> getClients(){
		return clientSockets;
	}

	public int getPort() {
		return serverSocket.getLocalPort();
	}
	
	public synchronized boolean isAccepting(){
		return accepting;
	}
	
	/**
	 * Remove dead sockets
	 */
	public void removeDead(){
		
		ListIterator<IClientSocket> li = clientSockets.listIterator();
		ArrayList<IClientSocket> remove = new ArrayList<IClientSocket>();
		while(li.hasNext()){
			IClientSocket client = li.next();
			if(!client.isRunning())
				remove.add(client);
		}
		for(IClientSocket c : remove){
			c.close("dead");
			clientSockets.remove(c);
		}
	}
	
	public void sendBroadcast(String message){
		ListIterator<IClientSocket> li = clientSockets.listIterator();
		
		while(li.hasNext()){
			IClientSocket client = li.next();
			if(client.isRunning() && client.getUsername() != null)
				client.sendMessage(message);
		}
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
			accepting = true;
			try {
				Socket clientSocket = serverSocket.accept();
				removeDead();
				IClientSocket clientThread = new ClientSocket(clientSocket, this);
				if(clientSockets.size() >= maxConnections){
					clientThread.close("max connections reached");
				}else{
					clientSockets.add(clientThread);
					System.out.println("Added client, client count: " + clientSockets.size());
				}
			} catch (IOException e) {
				if(running)
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
