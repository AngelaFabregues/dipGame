package es.csic.iiia.fabregues.dip.comm;

import es.csic.iiia.fabregues.utilities.Interface;

/**
 * Communication interface. Notice that the most popular IComm is DaideComm.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public interface IComm{

	public String getName();

	/**
	 * Init communication
	 * @param comm
	 * @param log
	 * @throws StartingCommException
	 */
	public void init(Comm comm, Interface log) throws StartingCommException;

	/**
	 * Sends a message interface Language
	 * @param msg
	 */
	public void sendMessage(String[] msg) throws CommException;

	/**
	 * Handles the reception of a message using interface Language
	 * @param msg
	 */
	public void receivingMessage(String[] msg);
	
	/**
	 * Stop communication
	 */
	public void stop();


}
