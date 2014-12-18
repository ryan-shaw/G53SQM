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
	 * 			throws if issue disconnecting clients
	 */
	public void stopServer() throws IOException;
	
	/**
	 * Is the server accepting connections
	 * @return accepting
	 * 			true if clients can connect
	 */
	public boolean isAccepting();
	
	/**
	 * Remove dead clients
	 */
	public void removeDead();
	
	/**
	 * Send broadcast message to all connected clients
	 * @param message
	 * 			message to broadcast to all clients
	 */
	public void sendBroadcast(String message);
	
	/* Getters and setters */
	public ArrayList<IClientSocket> getClients();

	public int getPort();
		
}
