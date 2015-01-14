package client;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import vc.min.chat.Client.Client;
import vc.min.chat.Client.KeepAliveManager;
import vc.min.chat.Server.Server;
import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet2KeepAlive;
import vc.min.chat.Shared.Packets.Packet3Message;
import vc.min.chat.Shared.Packets.Packet4ListClients;
import vc.min.chat.Shared.Packets.Packet5PM;
import vc.min.chat.Shared.Packets.PacketHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, KeepAliveManager.class})
public class clientTest {
	private static Socket connection;
	private static DataOutputStream dos;
	private static DataInputStream dis;
	private static PacketHandler packetHandler;
	
	Packet0Login packet0 = new Packet0Login("testUsername");
	Packet2KeepAlive packet2keepalive = new Packet2KeepAlive();
	
	@Before
	public void setUp() throws InterruptedException, IOException{
		
		try {
			// Create the connection to the chat server
            connection = new Socket("localhost", 6111);
            
            // Prepare the input stream and store it in an instance variable
            dis = new DataInputStream(connection.getInputStream());
            dos = new DataOutputStream(connection.getOutputStream());
            packetHandler = new PacketHandler(dis, dos);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		byte b = dis.readByte();
		assertEquals(127, b);
	}
	
	
	@Test
	public void testLogin() throws IOException{
		
		DataOutputStream dos = Mockito.mock(DataOutputStream.class);
		packet0.write(dos);
		Mockito.verify(dos).writeUTF("testUsername");
	}
	
	@Test
	public void testLoginReceived() throws IOException{
		
		packetHandler.writePacket(packet0);
		
		byte b = dis.readByte();
		assertEquals(0, b);
		assertEquals("testUsername", dis.readUTF());
	}
	
	@Test
	public void testKeepAlive() throws IOException{
		
		packetHandler.writePacket(packet2keepalive);
		
		byte b = dis.readByte();
		assertEquals(2, b);
	}
	
	@Test
	public void testSendMessage() throws IOException{
		
	}

}
