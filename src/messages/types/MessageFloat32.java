package messages.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageFloat32 extends MessageValue {
	public static final String TYPE_NAME = "float32"; // should match column types in DB
	
	public float value;
	
	public MessageFloat32(float value, String fieldName) {
		super(fieldName);
		this.value = value;
	}
	
	public byte[] getBytes() {
		byte[] bfr = new byte[4];
        int pr_item = Float.floatToRawIntBits(this.value);
        bfr[3] = (byte) ((byte)(pr_item >>> 24 ) & 0xff);
        bfr[2] = (byte) ((byte)(pr_item >>> 16 ) & 0xff);
        bfr[1] = (byte) ((byte)(pr_item >>>  8 ) & 0xff);
        bfr[0] = (byte) ((byte)(pr_item >>>  0 ) & 0xff);
        return bfr;
	}
	
	public int getSize() {
		return 4;
	}
	
	@Override
	public String toString() {
		return Float.toString(value);
	}
	
	@Override
	public String getType() {
		return "float32";
	}

	public void addValueQuery(PreparedStatement pstmt, int index) throws SQLException {
		pstmt.setFloat(index, this.value);
	}
}
