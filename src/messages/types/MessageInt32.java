package messages.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageInt32 extends MessageValue {
	public static final String TYPE_NAME = "int32"; // should match column types in DB
	public int value;
	
	public MessageInt32(int value, String fieldName) {
		super(fieldName);
		this.value = value;
	}
	
	public byte[] getBytes() {
		byte[] bfr = new byte[4];
        bfr[3] = (byte) ((byte)(value >>> 24 ) & 0xff);
        bfr[2] = (byte) ((byte)(value >>> 16 ) & 0xff);
        bfr[1] = (byte) ((byte)(value >>>  8 ) & 0xff);
        bfr[0] = (byte) ((byte)(value >>>  0 ) & 0xff);
        return bfr;
	}
	
	public int getSize() {
		return 4;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public String getType() {
		return "int32";
	}

	@Override
	public void addValueQuery(PreparedStatement pstmt, int index) throws SQLException {
		pstmt.setInt(index, this.value);
	}
}
