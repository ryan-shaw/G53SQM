package server;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import vc.min.chat.Server.Server;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.Packet3Message;
import vc.min.chat.Shared.Packets.Packet4ListClients;
import vc.min.chat.Shared.Packets.Packet5PM;
import vc.min.chat.Shared.Packets.PacketHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class IntegrationTests {
	private static Server server;
	private static Socket client;
	private static DataOutputStream dos;
	private static DataInputStream dis;
	private static PacketHandler p;
	
	@Rule
	public Timeout globalTimeout = new Timeout(5000);
	
	@Before
	public void setUp() throws InterruptedException, IOException{
		server = new Server(0, 2);
		new Thread(server).start();
		while(!server.isAccepting()); // Wait for server to accept clients
		
		try {
			client = new Socket("localhost", server.getPort());
			dos = new DataOutputStream(client.getOutputStream());
			dis = new DataInputStream(client.getInputStream());
			p = new PacketHandler(dis, dos);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		byte b = dis.readByte();
		assertEquals(127, b);
	}
	
	@After
	public void tearDown() throws InterruptedException{
		try {
			server.stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void login() throws IOException{
		Packet0Login packetLogin = new Packet0Login("test");
		p.writePacket(packetLogin);
		
		byte b = dis.readByte();
		assertEquals(0, b);
		assertEquals("test", dis.readUTF());
	}
	
	@Test
	public void testMultipleLogins2Sockets() throws InterruptedException, UnknownHostException, IOException{
		login();
		TestClient client1 = new TestClient("test1", server.getPort());
		client1.close();
	}
	
	@Test
	public void testMultipleLogins() throws InterruptedException, IOException {		
		Packet0Login packet = new Packet0Login("test");
		p.writePacket(packet);
		// Expect a confirmation
		byte b = dis.readByte();
		assertEquals(0, b);
		assertEquals("test", dis.readUTF());
	
		Packet0Login packet1 = new Packet0Login("test1");
		p.writePacket(packet1);
		// Expect a confirmation
		b = dis.readByte();
		assertEquals(0, b);
		assertEquals("test1", dis.readUTF());
		
		// Send another login packet should just change the username.
		
		return;
	}
	
	@Test
	public void testLogin() throws InterruptedException, IOException {
		Packet0Login packet = new Packet0Login("test");
		p.writePacket(packet);
		// Expect a confirmation
		byte b = dis.readByte();
		assertEquals(0, b);
		assertEquals("test", dis.readUTF());
	
		return;
	}
	
	@Test
	public void testDisconnect() throws IOException{
		Packet1Disconnect packet255disconnect = new Packet1Disconnect("test dc");
		p.writePacket(packet255disconnect);
		byte b = dis.readByte();
		assertEquals(1, b);
		assertEquals("test dc", dis.readUTF());
		return;
	}
	
	@Test
	public void testKeepAlive() throws InterruptedException, IOException{
		Packet2KeepAlive packet2keepalive = new Packet2KeepAlive();
		p.writePacket(packet2keepalive);
		byte b = dis.readByte();
		assertEquals(2, b);
		
		Thread.sleep(1200);
		// After timeout exceeds 1000 seconds we should receive a disconnect packet
		b = dis.readByte();
		assertEquals(1, b);
	}
	
	@Test
	public void testMalformedPacket() throws InterruptedException, IOException{
		dos.writeByte(10);
		assertEquals(1, dis.readByte());
	}
	
	@Test
	public void testMessages() throws InterruptedException, UnknownHostException, IOException{
		login();
		TestClient client1 = new TestClient("test1", server.getPort());

		Packet3Message packet3message = new Packet3Message("test message", "test1");
		p.writePacket(packet3message);
		Packet3Message packet3message1 = (Packet3Message) client1.pHandler.readPacket(client1.dis.readByte());
		assertEquals("test message", packet3message1.message);
		assertEquals("test", packet3message1.from);
		
		client1.close();
	}
	
	@Test
	public void testPacketOrder() throws IOException{
		Packet3Message packet3message = new Packet3Message("test message", "test");
		p.writePacket(packet3message);
		byte b = dis.readByte();
		assertEquals(1, b);
	}
	
	@Test
	public void testConnectionLimit() throws UnknownHostException, IOException, InterruptedException{
		// Connection limit for test server is 2
		TestClient client1 = new TestClient("test1", server.getPort());
		TestClient client2;
		try{
			client2 = new TestClient("test2", server.getPort());
			fail("Didn't throw ClassCastException");
			client2.close();
			client1.close();
		}catch(ClassCastException e){
			// Success if this is thrown
		}
	}
	
	@Test
	public void testListClientsCount() throws IOException, InterruptedException{
		login();

		Packet4ListClients packet = new Packet4ListClients(false);
		p.writePacket(packet);
		
		byte b = dis.readByte();
		assertEquals(4, b);
		Packet4ListClients packet4listclients = (Packet4ListClients) p.readPacket(4);
		assertEquals(1, packet4listclients.count);
	}
	
	@Test
	public void testListClients() throws IOException{
		login();
		
		Packet4ListClients packet = new Packet4ListClients(true);
		p.writePacket(packet);
		
		byte b = dis.readByte();
		assertEquals(4, b);
		Packet4ListClients packet4listclients = (Packet4ListClients) p.readPacket(4);
		assertEquals(1, packet4listclients.count);
		assertEquals("test", packet4listclients.clients.get(0));
	}
	
	@Test(expected=ClassCastException.class)
	public void testUsernameExists() throws UnknownHostException, IOException{
		login();
		TestClient client1 = new TestClient("test", server.getPort());
		client1.close();
	}
	
	@Test
	public void testPrivateMessage() throws IOException{
		login();
		TestClient client1 = new TestClient("test1", server.getPort());
		
		Packet5PM packet5pm = new Packet5PM("test1", "spoofed", "test message");
		p.writePacket(packet5pm);

		Packet3Message packet5pmR = (Packet3Message) client1.pHandler.readPacket(client1.dis.readByte());

		assertEquals("test message", packet5pmR.message);
		assertEquals("test", packet5pmR.from);
		
		packet5pm = new Packet5PM("test2", "test", "test message, should not deliver");
		p.writePacket(packet5pm);
		packet5pmR = (Packet3Message) p.readPacket(dis.readByte());
		
		assertEquals("user not found", packet5pmR.message);
		assertEquals("SERVER", packet5pmR.from);
		
		client1.close();
	}
	
	@Test
	public void messageTransitTime() throws UnknownHostException, IOException, InterruptedException{
		login();
		TestClient client2 = new TestClient("test2", server.getPort());
		long total = 0;
		for(int i = 0; i < 15; i++){
			long start = System.nanoTime();
			p.writePacket(new Packet5PM("test2", "test", "test"));
			client2.pHandler.readPacket(client2.dis.readByte());
			long stop = System.nanoTime();
			if(i != 0)
			total += stop-start;
			System.out.println(stop-start);
			client2.pHandler.writePacket(new Packet2KeepAlive());
			Thread.sleep(50);
		}
		System.out.println("Total: " + total/15);
	}
	
}

class TestClient{
	public DataOutputStream dos;
	public DataInputStream dis;
	public PacketHandler pHandler;
	private Socket client;
	public TestClient(String username, int port) throws UnknownHostException, IOException{
		client = new Socket("localhost", port);
		try{
			dos = new DataOutputStream(client.getOutputStream());
			dis = new DataInputStream(client.getInputStream());
			pHandler = new PacketHandler(dis, dos);
		}catch(IOException e){
			e.printStackTrace();
			client.close();
			return;
		}
		checkGreeting();
		performLogin(username);
	}
	
	private void checkGreeting() throws IOException{
		assertEquals(127, dis.readByte());
	}
	
	private void performLogin(String username) throws IOException{
		Packet0Login packetLogin = new Packet0Login(username);
		pHandler.writePacket(packetLogin);
		byte b = dis.readByte();
		Packet0Login returnedPacket = (Packet0Login) pHandler.readPacket(b);
		assertEquals(username, returnedPacket.username);

	}
	
	public void close() throws IOException{
		dos.close();
		dis.close();
		client.close();
	}
}
