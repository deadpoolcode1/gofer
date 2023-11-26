package serial_comm;

import java.util.ArrayList;
import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import db.DataBase;
import messages.*;
import messages.types.*;

public class MessageListener implements SerialPortMessageListener {
   
	
	public DataBase db;
	public MessageListener() {
		super();
		this.db = new DataBase();
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		// called back by jSerial when a message delimited by end sync bytes is received from the UUT
    	System.out.println("Message Recieved!");
		byte[] rawMsg = event.getReceivedData();
		System.out.println("DATA SIZE:\n"+event.getReceivedData().length);
    	MessageHeader msgHeader = new MessageHeader(rawMsg);
    	MessageTail msgTail = new MessageTail(rawMsg);
    	
    	// CREATE BODY
    	db.connect();
		MessageBody msgBody = db.getBody(msgHeader, MessageBody.getReader(rawMsg));
    	Message msg = new Message(msgBody, msgHeader, msgTail);
    	System.out.println("New Message:\n"+msg);
    	msg.uploadMessage(db);
		db.disconnect();
	}

	@Override
	public boolean delimiterIndicatesEndOfMessage() {
		return true;
	}

	@Override
	public byte[] getMessageDelimiter() {
		return new byte[] { (byte)0x0B, (byte)0x65 };
	}

}
