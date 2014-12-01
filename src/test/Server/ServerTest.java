package test.Server;

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
import vc.min.chat.Shared.Packets.PacketHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class ServerTest {
	private static Server server;
	private static Socket client;
	private static DataOutputStream dos;
	private static DataInputStream dis;
	private static PacketHandler p;
	
	@Rule
	public Timeout globalTimeout = new Timeout(5000);
	
	@Before
	public void setUp() throws InterruptedException{
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
		while(!client.isConnected()); // Wait for connection to be established
	}
	
	@After
	public void tearDown() throws InterruptedException{
		Thread.sleep(100);
		try {
			server.stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread.sleep(500);
	}
	
	@Test
	public void testMultipleLogins2Sockets() throws InterruptedException, UnknownHostException, IOException{		
		Socket lclient = new Socket("localhost", server.getPort());
		DataOutputStream ldos = new DataOutputStream(lclient.getOutputStream());
		DataInputStream ldis = new DataInputStream(lclient.getInputStream());
		PacketHandler lp = new PacketHandler(ldis, ldos);
//		Thread.sleep(200); // Wait for client connect
		while(!lclient.isConnected());
		
		Packet0Login packet = new Packet0Login("test");
		try {
			p.writePacket(packet);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		Thread.sleep(100);
		// Expect a confirmation
		try {
			byte b = dis.readByte();
			assertEquals(0, b);
			assertEquals("test", dis.readUTF());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		Packet0Login lpacket = new Packet0Login("test1");
		try {
			lp.writePacket(lpacket);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		Thread.sleep(100);
		// Expect a confirmation
		try {
			byte b = ldis.readByte();
			assertEquals(0, b);
			assertEquals(ldis.readUTF(), "test1");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		lclient.close();
	}
	
	@Test
	public void testMultipleLogins() throws InterruptedException {
		Thread.sleep(100);
		
		Packet0Login packet = new Packet0Login("test");
		try {
			p.writePacket(packet);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		Thread.sleep(100);
		// Expect a confirmation
		try {
			byte b = dis.readByte();
			assertEquals(0, b);
			assertEquals("test", dis.readUTF());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		Packet0Login packet1 = new Packet0Login("test1");
		try {
			p.writePacket(packet1);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		Thread.sleep(100);
		// Expect a confirmation
		try {
			byte b = dis.readByte();
			assertEquals(0, b);
			assertEquals("test1", dis.readUTF());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		// Send another login packet should just change the username.
		
		return;
	}
	
	@Test
	public void testLogin() throws InterruptedException {
		Thread.sleep(100);
		
		Packet0Login packet = new Packet0Login("test");
		try {
			p.writePacket(packet);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		Thread.sleep(100);
		// Expect a confirmation
		try {
			byte b = dis.readByte();
			assertEquals(0, b);
			assertEquals("test", dis.readUTF());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		return;
	}
	
	@Test
	public void testDisconnect(){
		Packet1Disconnect packet255disconnect = new Packet1Disconnect("test dc");
		try {
			p.writePacket(packet255disconnect);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		try {
			byte b = dis.readByte();
			assertEquals(1, b);
			assertEquals("test dc", dis.readUTF());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		return;
	}
	
	@Test
	public void testKeepAlive() throws InterruptedException{
		Packet2KeepAlive packet2keepalive = new Packet2KeepAlive();
		try {
			p.writePacket(packet2keepalive);
		} catch (IOException e2) {
			fail(e2.getMessage());
		}
		
		try {
			byte b = dis.readByte();
			assertEquals(2, b);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		
		Thread.sleep(1200);
		try {
			// After timeout exceeds 1000 seconds we should receive a disconnect packet
			byte b = dis.readByte();
			assertEquals(1, b);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testMalformedPacket() throws InterruptedException{
		try {
			dos.writeByte(10);
		} catch (IOException e) {
			fail(e.getMessage());
		} // Packet with byte 10 doesn't exist
		Thread.sleep(100);
		try {
			assertEquals(1, dis.readByte());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
