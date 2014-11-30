package test.Server;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import vc.min.chat.Server.Server;
import vc.min.chat.Shared.Packets.Login0Packet;
import vc.min.chat.Shared.Packets.PacketHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ServerTest {
	private Server server;
	private Socket client;
	private DataOutputStream dos;
	private DataInputStream dis;
	private PacketHandler p;
	@Before
	public void setUp(){
		server = new Server(0, 2);
		new Thread(server).start();
		
		try {
			Thread.sleep(200); // Wait for server to spin up
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			client = new Socket("localhost", server.getPort());
			dos = new DataOutputStream(client.getOutputStream());
			dis = new DataInputStream(client.getInputStream());
			p = new PacketHandler(dis, dos);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testLogin() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Login0Packet packet = new Login0Packet("test");
		try {
			p.writePacket(packet);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		
		// Expect a confirmation
		try {
			byte b = dis.readByte();
			assertEquals(b, 0);
			assertEquals(dis.readUTF(), "test");
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
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
