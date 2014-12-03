package vc.min.chat.Server;

import java.io.IOException;
import java.util.ArrayList;

public interface IServer {
	void stopServer() throws IOException;
	
	ArrayList<ClientSocket> getClients();

	int getPort();
	
	boolean isAccepting();
	
	void removeDead();
}
