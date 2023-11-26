package db;

import java.sql.SQLException;
import java.sql.ResultSet;

/*
 * This Class represents a row in the DB.
 */

public class DataBaseHeader {
	int rowID;
	String type, header, body;
	boolean read = false;
	
	public DataBaseHeader (ResultSet rs) throws SQLException  {
		this.read = rs.getInt("read") == 1;
		this.rowID = rs.getInt("rowid");
		this.type = rs.getString("type");
		this.header = rs.getString("header");
		this.body = rs.getString("body");
	}
	
	public DataBaseHeader(int rowID, String type, String header, String body, boolean read) {
		this.rowID = rowID;
		this.type = type;
		this.header = header;
		this.body = body;
		this.read = read;
	}
	
	@Override
    public String toString() {
		String result = "\n****************************";
		
		result += "\nROWID: " + this.rowID;
		result += "\ntype: " + this.type;
		result += "\nheader: " + this.header;
		result += "\nbody: " + this.body;
		result += "\nread: " + this.read;
		result += "\n****************************\n";
		
        return result;
    }
	
}
