package messages.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageBoolean extends MessageValue {
	public static final String TYPE_NAME = "boolean"; // should match column types in DB
	
	public boolean value;
	
	public MessageBoolean(boolean value, String fieldName) {
		super(fieldName);
		this.value = value;
	}
	
	public byte[] getBytes() {
		byte[] bfr = new byte[4];
        bfr[1]=bfr[2]=bfr[3]=0;
        if(this.value)
            bfr[0] = 1;
        else
            bfr[0] = 0;
        return bfr;
	}
	
	public int getSize() {
		return 4;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
	@Override
	public String getType() {
		return "boolean";
	}

	public void addValueQuery(PreparedStatement pstmt, int index) throws SQLException {
		pstmt.setInt(index, this.value ? 1 : 0);
	}
}
