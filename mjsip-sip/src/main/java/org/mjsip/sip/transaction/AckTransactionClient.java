/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 */

package org.mjsip.sip.transaction;



import org.mjsip.sip.message.SipMessage;
import org.mjsip.sip.provider.SipId;
import org.mjsip.sip.provider.SipProvider;
import org.slf4j.LoggerFactory;



/** ACK client transaction should follow an INVITE client transaction within an INVITE Dialog in a SIP UAC.
  * The AckTransactionClient simply sends an ACK request message and terminates.
  */ 
public class AckTransactionClient extends Transaction {
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AckTransactionClient.class);

	/** the TransactionClientListener that captures the events fired by the AckTransactionClient */
	TransactionClientListener transaction_listener;


	/** Creates a new AckTransactionClient. */
	public AckTransactionClient(SipProvider sip_provider, SipMessage ack, TransactionClientListener listener) {
		super(sip_provider);
		request=new SipMessage(ack);
		transaction_listener=listener;
		transaction_id=SipId.createTransactionClientId(request);
		LOG.info("new transaction-id: {}", transaction_id);
	}
	
	/** Starts the AckTransactionClient and sends the ACK request. */
	public void request() {
		LOG.trace("start");
		sip_provider.sendMessage(request);
		changeStatus(STATE_TERMINATED);  
		//if (transaction_listener!=null) transaction_listener.onAckCltTerminated(this); 
		// (CHANGE-040421) free the link to transaction_listener
		transaction_listener=null;
	}     
			
	/** Method used to drop an active transaction. */
	@Override
	public void terminate() {
		changeStatus(STATE_TERMINATED);  
		// (CHANGE-040421) free the link to transaction_listener
		transaction_listener=null;
	}
}
