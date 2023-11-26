package messages.types;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;

public class MessageByte20 extends MessageValue {
	public static final String TYPE_NAME = "byte20"; // should match column types in DB
	public byte[] value;

	public MessageByte20(byte[] bytes,String fieldName) {
		super(fieldName);
		this.value = bytes;
	}


	public MessageByte20(Blob blob, String fieldName) throws SQLException {
		super(fieldName);
		this.value = blob.getBytes(1,(int) blob.length());
		this.value = Arrays.copyOf(this.value, 20);
	}


	public String getType() {
		return "byte20";
	}

	public byte[] getBytes() {
		return this.value;
	}

	public void addValueQuery(PreparedStatement pstmt, int index) throws SQLException {
//		pstmt.setBlob(index, (Blob) new SerialBlob(this.value));
		pstmt.setBytes(index, this.value);
	}

	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < this.value.length; i++) {
			str += this.value[i] + 
				((i < this.value.length - 1) ? "," : "]");
		}
		return str;
	}

}
