package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This is the packet abstract class, all packets shall extend this class
 * 2 methods, {@link Packet#write} and {@link Packet#read}
 * 
 * @author Ryan Shaw
 *
 */
public abstract class Packet {
	/**
	 * Write the packet to dos
	 * @param dos
	 * 			the output stream
	 * @throws IOException
	 * 			if connection issue
	 */
	public abstract void write(DataOutputStream dos) throws IOException;
	
	/**
	 * Read the packet from dis
	 * @param dis
	 * 			the input stream
	 * @return this
	 * 			return this object
	 * @throws IOException
	 * 			if connection issue
	 */
	public abstract Packet read(DataInputStream dis) throws IOException;
}
