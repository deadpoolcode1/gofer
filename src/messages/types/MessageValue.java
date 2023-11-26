package messages.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MessageValue {
	public static final String TYPE_NAME = "value"; // should match column types in DB
	
	public String fieldName;
	
	public MessageValue(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public abstract String getType();
	public abstract byte[] getBytes();
	public abstract void addValueQuery(PreparedStatement pstmt, int index) throws SQLException;
	
	public int getSize() {
		return this.getBytes().length;
	}
	@Override
	public abstract String toString();
}
