package test.Server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import vc.min.chat.Server.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ServerTest {
	private Server server;
	private Socket client;
	
	@Before
	public void setUp(){
		server = new Server(4444, 2);
		new Thread(server).start();
	}
	
	@Test
	public void testConnection() {
		try {
			client = new Socket("localhost", 4444);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		return;
	}
	
	@After
	public void tearDown(){
		try {
			server.stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
