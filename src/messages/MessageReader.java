package messages;

public class MessageReader {
    // a set of methods to translate a sub-set array from a message byte array into JAVA primitives
    int ptr=0, max;
    byte[] buffer;

    public MessageReader(byte[] buffer) {
        this.ptr=0;
        this.max = buffer.length;
        this.buffer = buffer;
    }
    
    // set of reading methods
    public byte readByte() {
        byte x = buffer[ptr];
        ptr += 1;
        return x;
    }
    
    public boolean readBoolean() {
       int x = this.readInt();
        if(x != 0)
            return true;
        else
            return false;
    }
    
    public char readChar() {
        char x = (char)this.readShort();
        return x;
    }
    
    public short    readShort() {
        short x = 0;
        x = buffer[ptr+1];
        x = (short) (x << 8);
        x = (short) (x | (buffer[ptr+0] & 0xff));
        ptr += 2;
        return x;
    }
    
    public int readInt() {
        int x = 0;
        x = buffer[ptr+3];
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+2] & 0xff));
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+1] & 0xff));
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+0] & 0xff));
        ptr += 4;
        return x;
    }
    
    public float    readFloat() {
        int x = 0;
        x = buffer[ptr+3];
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+2] & 0xff));
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+1] & 0xff));
        x = (int) (x << 8); x = (int) (x | (buffer[ptr+0] & 0xff));
        float xf = Float.intBitsToFloat(x);
        ptr += 4;
        return xf;
    }
    
    public long readLong() {
        long x = 0;
        x = buffer[ptr+7];
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+6] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+5] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+4] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+3] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+2] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+1] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+0] & 0xff));
        ptr += 8;
        return x;
    }
    
    public double readDouble() {
        long x = 0;
        x = buffer[ptr+7];
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+6] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+5] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+4] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+3] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+2] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+1] & 0xff));
        x = ( long) (x << 8); x = ( long) (x | (buffer[ptr+0] & 0xff));
        double xf = Double.longBitsToDouble(x);
        ptr += 8;
        return xf;
    }
    
    public byte[] readBytesArray() {
    	byte[] bytes = new byte[20];
        int i=0;
        for( ;i<bytes.length; i++)
        	bytes[i] = buffer[ptr+i];
        ptr += i;
        return bytes;
    }
}
