package messages;

import java.util.Arrays;

import messages.types.Address;

public class MessageHeader {
	
	private final static int SIZE = 4+4+4+2*Address.getSize()+4; // comm header size=32
	
	public int length=0;
    public int sendTimeMs=0;
    public int serialNumber=0;
    public Address destAddress, senderAddress;
    public int msgCode = 0;
    
    public MessageHeader(byte[] rawMsg) {
    	int msgLenth = rawMsg.length;
    	MessageReader msgRdr = new MessageReader(Arrays.copyOfRange(rawMsg, 0, SIZE));
    	
    	// Order of reading is important - indicates order in actual message
    	this.length = msgRdr.readInt();
    	this.sendTimeMs = msgRdr.readInt();
    	this.serialNumber = msgRdr.readInt();
    	this.destAddress = new Address(msgRdr);
    	this.senderAddress = new Address(msgRdr);
    	this.msgCode = msgRdr.readInt();
    	
    	// TODO: Add validators (length/etc)
    }

    public MessageHeader(
            int length,
            int sendTimeMs,
            int serialNumber,
            Address destAddress,
            Address senderAddress,
            int msgCode
             ) {
        this.length = length;
        this.sendTimeMs = sendTimeMs;
        this.serialNumber = serialNumber;
        this.destAddress = destAddress;
        this.senderAddress = senderAddress;
        this.msgCode = msgCode;
        
    }
    
    public static int getSize() {
    	return SIZE;
    }
    
    public byte[] getBytes() {
        byte[] bfr = new byte[SIZE];
        MessageBuilder MB = new MessageBuilder(bfr);
        MB.Add(MB.getBytes(length));
        MB.Add(MB.getBytes(sendTimeMs));
        MB.Add(MB.getBytes(serialNumber));
        MB.Add(destAddress.getBytes());
        MB.Add(senderAddress.getBytes());
        MB.Add(MB.getBytes(msgCode));
        return bfr;
    }
    
    @Override
    public String toString() {
    	String hdr = "Length: " + this.length
    			   + "\nTime: " + this.sendTimeMs
    			   + "\nSerial Number: " + this.serialNumber
    			   + "\nDest Address: \n" + this.destAddress
    			   + "\nSender Address: \n" + this.senderAddress
    			   + "\nMsg Code: " + this.msgCode;
        return hdr; 
    }
}
