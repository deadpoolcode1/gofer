package messages;

import db.DataBase;

public class Message {
	public static final int MAX_LENGTH = 246;
	
	public int serialNumber = 0;
	public int msgCode = 0;
	public MessageBody msgBody = null;
	public MessageHeader msgHeader = null;
	public MessageTail msgTail = null;
	private int length = 0;
	
	public Message(MessageBody msgBody, MessageHeader msgHeader) {
		this.msgBody = msgBody;
		this.msgHeader = msgHeader;
		this.serialNumber = msgHeader.serialNumber;
		this.msgCode = msgHeader.msgCode;
		this.msgTail = new MessageTail();
		this.length = msgBody.getSize() + MessageHeader.getSize() + MessageTail.getSize();
		this.msgHeader.length = this.length;
		this.msgTail.setChecksum(this.msgHeader, this.msgBody);
	}
	
	public Message(MessageBody msgBody, MessageHeader msgHeader, MessageTail msgTail) {
		this.msgBody = msgBody;
		this.msgHeader = msgHeader;
		this.serialNumber = msgHeader.serialNumber;
		this.msgCode = msgHeader.msgCode;
		this.msgTail = msgTail;
		this.length = msgBody.getSize() + MessageHeader.getSize() + MessageTail.getSize();
		this.msgHeader.length = this.length;
		
		// TODO: Add Validators for rawMsg. (length/checksum/etc..)
	}
	
	
	public void sendMessage() {
		// sends data as byte[] to LMDS
		
		System.out.println("Message from DB: \n" + this.toString());
	}
	
	public void uploadMessage(DataBase db) {
		db.uploadHeader(this.msgHeader);
		db.uploadBody(this.msgBody, this.msgHeader);
	}
	
	@Override
	public String toString() {
		String msg = "****************************************"
				   + "\nHeader: \n" + this.msgHeader
				   + "\n****************************************"
				   + "\nBody: \n" + this.msgBody
				   + "\n****************************************"
				   + "\nTail: \n" + this.msgTail
		   		   + "\n****************************************";
		return msg;
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[this.length];
		int i = 0;
		for (; i < MessageHeader.getSize(); i++)
			bytes[i] = this.msgHeader.getBytes()[i];
		for (; i - MessageHeader.getSize() < this.msgBody.getSize(); i++)
			bytes[i] = this.msgBody.getBytes()[i - MessageHeader.getSize()];
		for (; i - MessageHeader.getSize() - this.msgBody.getSize() < MessageTail.getSize(); i++)
			bytes[i] = this.msgTail.getBytes()[i - MessageHeader.getSize() - this.msgBody.getSize()];
		return bytes;
	}
	
	public int getLength() {
		return this.length;
	}

}
