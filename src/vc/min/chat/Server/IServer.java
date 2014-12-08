package vc.min.chat.Server;

import java.io.IOException;
import java.util.ArrayList;

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
	
	/* Getters and setters */
	public ArrayList<ClientSocket> getClients();

	public int getPort();
		
}
