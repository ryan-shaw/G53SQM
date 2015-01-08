package vc.min.chat.Server;

import java.io.IOException;

import vc.min.chat.Server.IO.IClientSocket;
import vc.min.chat.Server.Logger.LogLevel;
import vc.min.chat.Server.Logger.Logger;

/**
 * Checks the last read time of all connected clients to make sure they are alive
 * 
 * @author Ryan Shaw
 *
 */
public class PingChecker extends Thread{

	private Server server;
	
	public PingChecker(Server server){
		this.server = server;
	}
	
	public void run(){
		Logger.log(LogLevel.INFO, "Ping checker started");
		while(true){
			for(IClientSocket client : server.getClients()){
				/* Check if the client has sent something recently */
				if(System.currentTimeMillis() - client.getLastTimeRead() > 1000L && client.isRunning()){
					Logger.log(LogLevel.INFO, "Client timed out: " + client.getUsername());
					try {
						client.close("timeout reached");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
