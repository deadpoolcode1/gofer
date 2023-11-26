package messages;

import java.util.ArrayList;
import java.util.Arrays;

import messages.types.MessageValue;

public class MessageBody {
	public int serNum = 0;
	public int msgCode = 0;
	public ArrayList<MessageValue> values;
	
	private int length = 0;
	
	public MessageBody(int serNum, int msgCode, ArrayList<MessageValue> values) {
		this.serNum = serNum;
		this.msgCode = msgCode;
		this.values = values;
		this.length = this.getSize();
	}
	
	public MessageBody(byte[] rawMsg) {
		byte[] bodyBytes = Arrays.copyOfRange(
				rawMsg, 
				MessageHeader.getSize(), 
				rawMsg.length - MessageTail.getSize()
			);
    	MessageReader msgRdr = new MessageReader(bodyBytes);
    	
    	
	}
	
	public static MessageReader getReader(byte[] rawMsg) {
		byte[] bodyBytes = Arrays.copyOfRange(
				rawMsg, 
				MessageHeader.getSize(), 
				rawMsg.length - MessageTail.getSize()
			);
    	return new MessageReader(bodyBytes);
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[this.getSize()];
		int index = 0;
		for(MessageValue value: this.values) {
			for(byte byteValue: value.getBytes()) {
				if (index < bytes.length) 
					bytes[index] = byteValue;
				index++;
			}
		}
		return bytes;
	}
	
	public int getSize() {
		int size = 0;
		for(MessageValue value: this.values) 
			size += value.getSize();
		return size;
	}
	
	@Override
	public String toString() {
		String valueList = "Values: ";
		
		for(MessageValue value: this.values) {
			valueList += "\nName: " + value.fieldName;
			valueList += "\tValue: " + value.toString();
		}
		valueList += "\nByte Array: \n" + Arrays.toString(getBytes());
		return valueList;
	}
}
