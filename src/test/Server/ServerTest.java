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
		
		byte b = ldis.readByte();
		assertEquals(127, b);
		
		Packet0Login packet = new Packet0Login("test");
		p.writePacket(packet);
		// Expect a confirmation
		b = dis.readByte();
		assertEquals(0, b);
		assertEquals("test", dis.readUTF());
		
		Packet0Login lpacket = new Packet0Login("test1");
		lp.writePacket(lpacket);
		// Expect a confirmation
		b = ldis.readByte();
		assertEquals(0, b);
		assertEquals(ldis.readUTF(), "test1");
		lclient.close();
	}
	
	@Test
	public void testMultipleLogins() throws InterruptedException, IOException {
		Thread.sleep(100);
		
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
		Thread.sleep(100);
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
		Thread.sleep(100);
		assertEquals(1, dis.readByte());
	}
	
	@Test
	public void testConnectionLimit() throws UnknownHostException, IOException, InterruptedException{
		// Connection limit for test server is 2
		Socket lclient1 = new Socket("localhost", server.getPort());
		DataOutputStream ldos1;
		DataInputStream ldis1;
		try{
			ldos1 = new DataOutputStream(lclient1.getOutputStream());
			ldis1 = new DataInputStream(lclient1.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
			lclient1.close();
			return;
		}
		Socket lclient2 = new Socket("localhost", server.getPort());
		DataOutputStream ldos2;
		DataInputStream ldis2;
		try{
			ldos2 = new DataOutputStream(lclient2.getOutputStream());
			ldis2 = new DataInputStream(lclient2.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
			lclient1.close();
			lclient2.close();
			return;
		}
		PacketHandler lp1 = new PacketHandler(ldis1, ldos1);
		PacketHandler lp2 = new PacketHandler(ldis2, ldos2);
		
		Packet0Login packet0login1 = new Packet0Login("test1");
		Packet0Login packet0login2 = new Packet0Login("test2");
		assertEquals(127, ldis1.readByte());
		assertEquals(127, ldis2.readByte());
		lp1.writePacket(packet0login1);
		lp2.writePacket(packet0login2);
		
		byte b = ldis1.readByte();
		assertEquals(0, b);
		b = ldis2.readByte();
		assertEquals(1, b);
		lclient1.close();
		lclient2.close();
	}
}
