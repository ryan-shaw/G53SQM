package test.Server;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import vc.min.chat.Shared.Packets.Packet0Login;
import vc.min.chat.Shared.Packets.Packet1Disconnect;
import vc.min.chat.Shared.Packets.Packet3Message;
import vc.min.chat.Shared.Packets.Packet4ListClients;
import vc.min.chat.Shared.Packets.Packet5PM;


/**
 * These are the unit tests for the packet server/client, the unit tests have to use 
 * PowerMockito so we are able to mock the DataInputStream (due it using final methods) 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataInputStream.class, Packet0Login.class, Packet1Disconnect.class, Packet3Message.class, Packet4ListClients.class, Packet5PM.class})
public class UnitTests {

	@Test
	public void testPacket0LoginWrite() throws IOException{
		Packet0Login packet = new Packet0Login("test");
		DataOutputStream dos = Mockito.mock(DataOutputStream.class);
		packet.write(dos);
		Mockito.verify(dos).writeUTF("test");
	}
	
	@Test
	public void testPacket0LoginRead() throws IOException{
		Packet0Login packet = new Packet0Login();
		DataInputStream dis = PowerMockito.mock(DataInputStream.class);
		PowerMockito.when(dis.readUTF()).thenReturn("test");
		packet.read(dis);
		assertEquals("test", packet.username);
	}
	
	@Test
	public void testPacket1DisconnectWrite() throws IOException{
		Packet1Disconnect packet = new Packet1Disconnect("test dc");
		DataOutputStream dos = Mockito.mock(DataOutputStream.class);
		packet.write(dos);
		Mockito.verify(dos).writeUTF("test dc");
	}
	
	@Test
	public void testPacket1DisconnectRead() throws IOException{
		Packet1Disconnect packet = new Packet1Disconnect();
		DataInputStream dis = PowerMockito.mock(DataInputStream.class);
		PowerMockito.when(dis.readUTF()).thenReturn("test dc");
		packet.read(dis);
		assertEquals("test dc", packet.message);
	}
	
	@Test
	public void testPacket3MessageWrite() throws IOException{
		Packet3Message packet = new Packet3Message("test message", "test");
		DataOutputStream dos = Mockito.mock(DataOutputStream.class);
		packet.write(dos);
		Mockito.verify(dos).writeUTF("test message");
		Mockito.verify(dos).writeUTF("test");
	}
	
	@Test
	public void testPacket3MessageRead() throws IOException{
		Packet3Message packet = new Packet3Message();
		DataInputStream dis = PowerMockito.mock(DataInputStream.class);
		PowerMockito.when(dis.readUTF()).thenReturn("test message").thenReturn("test");
		packet.read(dis);
		assertEquals("test message", packet.message);
		assertEquals("test", packet.from);
	}
	
	@Test
	public void testPacket4ListClientsWrite() throws IOException{
		ArrayList<String> clients = new ArrayList<String>();
		clients.add("test");
		clients.add("test1");
		Packet4ListClients packet = new Packet4ListClients(true, clients);
		DataOutputStream dos = PowerMockito.mock(DataOutputStream.class);
		packet.write(dos);
		Mockito.verify(dos).writeBoolean(false);
		Mockito.verify(dos).writeBoolean(true);
		Mockito.verify(dos).writeUTF("test, test1");
	}
	
	@Test
	public void testPacket4ListClientsRead() throws IOException{
		ArrayList<String> clients = new ArrayList<String>();
		clients.add("test");
		clients.add("test1");
		Packet4ListClients packet = new Packet4ListClients();
		DataInputStream dis = PowerMockito.mock(DataInputStream.class);
		PowerMockito.when(dis.readBoolean()).thenReturn(false /* client sending */ ).thenReturn(true /* full list*/ );
		PowerMockito.when(dis.readUTF()).thenReturn("test,test1");
		packet.read(dis);
		assertEquals(false, packet.clientSending);
		assertEquals(true, packet.fullList);
		assertEquals(clients, packet.clients);
	}
	
	@Test
	public void testPacket5PMWrite() throws IOException{
		Packet5PM packet = new Packet5PM("testto", "testfrom", "test message");
		DataOutputStream dos = PowerMockito.mock(DataOutputStream.class);
		packet.write(dos);
		Mockito.verify(dos).writeUTF("testto");
		Mockito.verify(dos).writeUTF("testfrom");
		Mockito.verify(dos).writeUTF("test message");
	}
	
	@Test
	public void testPacket5PMRead() throws IOException{
		Packet5PM packet = new Packet5PM();
		DataInputStream dis = PowerMockito.mock(DataInputStream.class);
		PowerMockito.when(dis.readUTF()).thenReturn("testto").thenReturn("testfrom").thenReturn("test message");
		packet.read(dis);
		assertEquals("testto", packet.toUsername);
		assertEquals("testfrom", packet.fromUsername);
		assertEquals("test message", packet.message);
		
	}
}
