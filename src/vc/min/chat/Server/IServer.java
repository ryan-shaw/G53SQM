package vc.min.chat.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Server interface provides generalised public methods for server management
 * 
 * @author Ryan Shaw
 *
 */
public interface IServer {

	/**
	 * Stops the server
	 * @throws IOException
	 */
	public void stopServer() throws IOException;
	
	/**
	 * Is the server accepting connections
	 * @return accepting
	 */
	public boolean isAccepting();
	
	/**
	 * Remove dead clients
	 */
	public void removeDead();
	
	/**
	 * Send broadcast message to all connected clients
	 */
	public void sendBroadcast(String message);
	
	/* Getters and setters */
	public ArrayList<IClientSocket> getClients();

	public int getPort();
		
}
