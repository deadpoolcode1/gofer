package messages;

import java.util.Arrays;

public class MessageTail {
	private static final int SIZE=6;
	
	private byte[] tail = null; 
    
	public MessageTail() {
		this.tail = new byte[]{0,0,0,0,0x0b, 0x65};// bytes 0-3 for checksum; 4-5 are sync bytes
		tail[4] = 0x0b;
        tail[5] = 0x65;
	}
	
	public MessageTail(byte[] rawMsg) {
		int msgLenth = rawMsg.length;
		this.tail = Arrays.copyOfRange(rawMsg, msgLenth - SIZE, msgLenth);
		
		// TODO: Add validators (checksum/length/etc)
	}

	public static int getSize() {
		return SIZE;
	}
	
//    public boolean isGoodChecksum(byte[] msg) {
//        // use in RX
//        int msg_chksum = GetMsgChecksum(msg, msg.length - 6);
//        int rx_chksum  = GetRxMsgChecksum(tail);
//        return msg_chksum == rx_chksum;
//    }

	private int getMsgChecksum(byte[] msg, int size) {
        int cs=0;
        for(int i=0;i<size;i++) {
            int k = (int)(msg[i]&0xff);
            cs +=k;
        }
        return cs;
    }
    public void setChecksum(MessageHeader msgHeader, MessageBody msgBody){
        // use on TX
    	int hdrSize = MessageHeader.getSize();
    	byte[] hdrBytes = msgHeader.getBytes();
    	byte[] bdyBytes = msgBody.getBytes();
    	byte[] msg = new byte[hdrSize+msgBody.getSize()];
    	int i = 0;
    	for (; i < hdrSize; i++)
    		msg[i] = hdrBytes[i];
    	for (; i < msg.length; i++)
    		msg[i] = bdyBytes[i - hdrSize];
        int cksum = getMsgChecksum(msg, msg.length);
        this.tail[0] = (byte)((cksum >>> 24)&0xff);
        this.tail[1] = (byte)((cksum >>> 16)&0xff);
        this.tail[2] = (byte)((cksum >>> 8)&0xff);
        this.tail[3] = (byte)(cksum&0xff);
    }
    public byte[] getBytes() {
        return this.tail;
    }
    
    
    @Override
	public String toString() {
    	return "Tail:\n" + Arrays.toString(this.tail);
    }
}
