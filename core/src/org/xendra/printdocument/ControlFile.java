package org.xendra.printdocument;

import java.io.Serializable;

import org.simoes.util.*;

/**
 * 
 * Represents the ControlFile sent with a print job when the command
 * is 02 Receive Print Job.  The control file contains information about
 * the print job.  The information is encapsulated in {@link ControlFileCommands}.    
 * The body of the print job itself is found at {@link DataFile}.
 * @author Chris Simoes
 *
 */
public class ControlFile extends PrintFile implements Cloneable {

	/**
	 * 
	 */
	private ControlFileCommands controlFileCommands;

	public ControlFile() {
		super();
	}

	public void setControlFileCommands(byte[] a) throws Exception {
		controlFileCommands = new ControlFileCommands(a);
	}
	
	public ControlFileCommands getControlFileCommands() {
		return this.controlFileCommands;
	}
	
	public Object clone() {
		final String METHOD_NAME = "clone()";
		ControlFile result = null;
		try {
			result = (ControlFile)super.clone();
			result.controlFileCommands = (ControlFileCommands)controlFileCommands.clone();
			result.setContents(this.getContents());
		} catch(CloneNotSupportedException e) {
			log.severe(METHOD_NAME + e.getMessage());
			throw new InternalError(METHOD_NAME + e.getMessage());
		}
		return result;
	}
}