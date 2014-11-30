package vc.min.chat.Server;

/**
 * Simple chat server based on the protocol listed in README.md
 * 
 * @author Ryan Shaw
 *
 */

public class Server {
	
	/**
	 * The server port
	 */
	private int port;
	
	/**
	 * Maximum number of connections to allow
	 */
	private int maxConnections;
	
	/**
	 * Convenience constructor
	 * 
	 * @param port
	 * @param maxConnections
	 */
	public Server(int port, int maxConnections){
		this.port = port;
		this.maxConnections = maxConnections;
	}
}
