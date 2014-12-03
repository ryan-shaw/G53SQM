package vc.min.chat.Server;

public class PingChecker extends Thread{

	private Server server;
	
	public PingChecker(Server server){
		this.server = server;
	}
	
	public void run(){
		while(true){
			for(ClientSocket client : server.getClients()){
				/* Check if the client has sent something recently */
				if(System.currentTimeMillis() - client.getLastTimeRead() > 1000L && client.isRunning()){
					System.out.println("Client timed out: " + client.getUsername());
					client.close("timeout reached");
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
