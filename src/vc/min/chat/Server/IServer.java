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
	 * @throws IOException 
	 */
	public void removeDead() throws IOException;
	
	/**
	 * Send broadcast message to all connected clients
	 * @param message
	 * 			message to broadcast to all clients
	 * @param from
	 * 			who from
	 */
	public void sendBroadcast(String from, String message);
	
	/* Getters and setters */
	public ArrayList<IClientSocket> getClients();

	public int getPort();
		
}
