package vc.min.chat.Shared.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
	/**
	 * Write the packet to dos
	 * @param dos
	 * @throws IOException
	 */
	public abstract void write(DataOutputStream dos) throws IOException;
	
	/**
	 * Read the packet from dis
	 * @param dis
	 * @return this
	 * @throws IOException
	 */
	public abstract Packet read(DataInputStream dis) throws IOException;
}
