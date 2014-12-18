package vc.min.chat.Server;

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
		while(true){
			for(IClientSocket client : server.getClients()){
				/* Check if the client has sent something recently */
				if(System.currentTimeMillis() - client.getLastTimeRead() > 1000L && client.isRunning()){
					System.out.println("Client timed out: " + client.getUsername());
					client.close("timeout reached");
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
