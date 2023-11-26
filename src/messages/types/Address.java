package messages.types;

import messages.MessageBuilder;
import messages.MessageReader;

public class Address {
    // address enums
//    public enum host_name_e {HOST_UNKNOWN,HOST_ATE_SERVER,HOST_LM,HOST_MPU,HOST_PCM,
//        HOST_AV_STACK,HOST_JPM,HOST_LUMO};
//    public enum process_name_e {PR_UNKNOWN,PR_TA1, PR_TA2, PR_PER, PR_TF1, PR_TF2};
    // class data
    public int hostName=0;
    public int processName=0;

    private final static int SIZE=8; // address size on a serial link

    public Address(int hostName, int processName) {
        this.hostName =  hostName;
        this.processName = processName;
    }
    
    public Address(MessageReader msgRdr) {
        this.hostName = msgRdr.readInt();
        this.processName = msgRdr.readInt();
    }
    
    public static int getSize() {
    	return SIZE;
    }
    
    public byte[] getBytes() {
        byte[] bfr = new byte[SIZE];
        MessageBuilder MB = new MessageBuilder(bfr);
        MB.Add(MB.getBytes(this.hostName));
        MB.Add(MB.getBytes(this.processName));
        return bfr;
    }
    
    @Override
	public String toString() {
    	return "Host: " + this.hostName + "\nProcess: " + this.processName;
    }
}
