package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import logger.Logger;
import messages.MessageBody;
import messages.MessageHeader;
import messages.MessageReader;
import messages.types.*;

public class DataBase {
	// change DB_URL with proper DB connection info.
	// for more info, see: https://docs.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?view=sql-server-ver15
	public static final String DB_URL = "//localhost:1433;databaseName=GOPHER;username=Gil;password=gilp7466";
	public static final String DB_PROMPT = "jdbc:sqlserver:";
	public static final String TS_MSG_TABLE = "ts_messages";
	public static final String MSG_CODE_LINK = "msg_code_link";
	
	private Connection conn = null;
	private ArrayList<MessageHeader> headerArr = null;
	
	public DataBase() {
		this.conn = null;
		this.headerArr = null;
	}
	
	public boolean isConnected() {
		return this.conn != null;
	}
	
    public void connect() {
    	if (!this.isConnected()) {
            try {
                // db parameters
                String url = DataBase.DB_PROMPT + DataBase.DB_URL;
                // create a connection to the database
                this.conn = DriverManager.getConnection(url);
                
                System.out.println("Connection to DB has been established.");
                
            } catch (SQLException ex) {
            	System.out.println("Encountered an error connecting to DB!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
            } 
    	}
    }
    
    public void disconnect() {
    	if (this.isConnected()) {
    		try {
    			this.conn.close();
    			this.conn = null;
    		} catch (SQLException ex) {
    			System.out.println("Encountered an error closing connection to DB!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
    		}
    	}
    }
    
    public ArrayList<MessageHeader> getUnreadHeaders() {
    	this.headerArr = new ArrayList<MessageHeader>();
    	String query = "SELECT * FROM " + DataBase.TS_MSG_TABLE
    				 + "\nWHERE read=FALSE";
    	
    	this.connect();
    	if (this.isConnected()) {
    		try(PreparedStatement pstmt  = conn.prepareStatement(query);
    			ResultSet rs = pstmt.executeQuery()) {
    			while (rs.next()) {
    				MessageHeader header = new MessageHeader(
    						rs.getInt("length"),
    						rs.getInt("time"),
    						rs.getInt("ser_num"),
    						new Address(rs.getInt("dest_host"),rs.getInt("dest_pr")),
    						new Address(rs.getInt("sender_host"),rs.getInt("sender_pr")),
    						rs.getInt("msg_code")
    					);
    				this.headerArr.add(header);
    			}
        		
        	} catch(SQLException ex) {
        		System.out.println("Encountered an error querying unread messages!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	} 
    	}
    	return this.headerArr;
    }
    
    public void updateToRead() {
    	this.connect();
    	if (this.isConnected()) {
    		try {
    			this.conn.setAutoCommit(false);
    			for(MessageHeader dbHeader: this.headerArr) {
    	    		String query = "UPDATE ts_messages SET read=TRUE WHERE ser_num="+ dbHeader.serialNumber +";";
    	    		PreparedStatement pstmt  = conn.prepareStatement(query);
    	    		pstmt.executeUpdate();
    	    	}
    			this.conn.commit();
        	} catch(SQLException ex) {
        		System.out.println("Encountered an error updating read messages!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    	}
    }
    
    // For reading from TS to LMDS (DB row to bytes)
    public MessageBody getBody(MessageHeader msgHeader) {
    	MessageBody msgBody = null;
    	this.connect();
    	if(this.isConnected()) {
    		try {
    			msgBody = new MessageBody(
    					msgHeader.serialNumber, 
    					msgHeader.msgCode, 
    					this.getStruct(msgHeader.msgCode, msgHeader.serialNumber)
    				);
    		} catch(SQLException ex) {
        		System.out.println("Encountered an error getting message body!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    		
    	}
    	return msgBody;
    }
    
    public ArrayList<MessageValue> getStruct(int msgCode, int serNum) throws SQLException {
    	ArrayList<MessageValue> values = new ArrayList<MessageValue>();
    	String query = "SELECT * FROM \"struct#" + msgCode 	// struct tables format "struct#<msg_code>"
    				 + "\" WHERE ser_num=" + serNum + ";";
    	PreparedStatement pstmt  = this.conn.prepareStatement(query);
    	ResultSet rs = pstmt.executeQuery();
    	if (rs.next()) {
    		ResultSetMetaData meta = rs.getMetaData();
        	String[] colNames = new String[meta.getColumnCount()];
        	for(int i = 1; i <= colNames.length; i++) {
        		String[] colName = meta.getColumnName(i).split(":"); // column names format "<field_name>:<type>"
        		String fieldName = colName[0];
        		if (fieldName.equals("ser_num")) {
        			continue;
        		}
        		String type = colName[1];
        		if (type.contains("struct")) {
        			int structNum = Integer.parseInt(type.split("#")[1]);
        			values.addAll(this.getStruct(structNum, serNum));
        		} else {
        			values.add(this.createType(fieldName, type, rs));
        		}
        	}
    	}
    	return values;
    }

    // returns matching MessageValueType for db column name type
    public MessageValue createType(String colName, String colType, ResultSet rs) throws SQLException{
    	MessageValue value = null;
    	
    	switch (colType) {
    		case "int32":
    			value = new MessageInt32(rs.getInt(colName+":"+colType), colName);
    			break;
    		case "float32":
    			value = new MessageFloat32(rs.getFloat(colName+":"+colType), colName);
    			break;
    		case "boolean":
    			value = new MessageBoolean(rs.getBoolean(colName+":"+colType), colName);
    			break;
    		case "byte20":
    			value = new MessageByte20(rs.getBytes(colName+":"+colType), colName);
    			break;
    		default:
    			break;
    	}
    	
    	return value;
    }
    
    
    // For reading from raw message (LMDS to TS)
    public MessageBody getBody(MessageHeader msgHeader, MessageReader msgRdr) {
    	MessageBody msgBody = null;
    	this.connect();
    	if(this.isConnected()) {
    		try {
    			msgBody = new MessageBody(
    					msgHeader.serialNumber, 
    					msgHeader.msgCode, 
    					this.getStruct(msgHeader.msgCode, msgHeader.serialNumber, msgRdr)
    				);
    		} catch(SQLException ex) {
        		System.out.println("Encountered an error creating message body!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    		
    	}
    	return msgBody;
    }
    
    public ArrayList<MessageValue> getStruct(int msgCode, int serNum, MessageReader msgRdr) throws SQLException {
    	ArrayList<MessageValue> values = new ArrayList<MessageValue>();
    	String query = "SELECT * FROM \"struct#" + msgCode + "\" LIMIT 1;";	// struct tables format "struct#<msg_code>"
    	
    	PreparedStatement pstmt  = this.conn.prepareStatement(query);
    	ResultSet rs = pstmt.executeQuery();
    	if (rs.next()) {
    		ResultSetMetaData meta = rs.getMetaData();
        	String[] colNames = new String[meta.getColumnCount()];
        	for(int i = 1; i <= colNames.length; i++) {
        		String[] colName = meta.getColumnName(i).split(":"); // column names format "<field_name>:<type>"
        		String fieldName = colName[0];
        		if (fieldName.equals("ser_num")) {
        			continue;
        		}
        		String type = colName[1];
        		if (type.contains("struct")) {
        			int structNum = Integer.parseInt(type.split("#")[1]);
        			values.addAll(this.getStruct(structNum, serNum));
        		} else {
        			values.add(this.createType(fieldName, type, msgRdr));
        		}
        	}
			
    	}
    	return values;
    }
    

    public MessageValue createType(String colName, String colType, MessageReader msgRdr) throws SQLException{
    	MessageValue value = null;
    	
    	switch (colType) {
    		case MessageInt32.TYPE_NAME:
    			value = new MessageInt32(msgRdr.readInt(), colName);
    			break;
    		case MessageFloat32.TYPE_NAME:
    			value = new MessageFloat32(msgRdr.readFloat(), colName);
    			break;
    		case MessageBoolean.TYPE_NAME:
    			value = new MessageBoolean(msgRdr.readBoolean(), colName);
    			break;
    		case "byte20":
    			value = new MessageByte20(msgRdr.readBytesArray(), colName);
    			break;
    		default:
    			break;
    	}
    	
    	return value;
    }
    
     // UPLOADING METHODS
    public void uploadHeader(MessageHeader msgHeader) {
    	this.connect();
    	if (this.isConnected()) {
    		try {
    			String query = "INSERT INTO " + TS_MSG_TABLE + 
	    				"(ser_num,read,sender_host,sender_pr,dest_host,dest_pr,time,msg_code,length)" + 
	    				" VALUES(?,?,?,?,?,?,?,?,?);";
	    		PreparedStatement pstmt  = conn.prepareStatement(query);
	    		pstmt.setInt(1, msgHeader.serialNumber);
	    		pstmt.setInt(2, 0); // new row => not read = 0
	    		pstmt.setInt(3, msgHeader.senderAddress.hostName);
	    		pstmt.setInt(4, msgHeader.senderAddress.processName);
	    		pstmt.setInt(5, msgHeader.destAddress.hostName);
	    		pstmt.setInt(6, msgHeader.destAddress.processName);
	    		pstmt.setInt(7, msgHeader.sendTimeMs);
	    		pstmt.setInt(8, msgHeader.msgCode);
	    		pstmt.setInt(9, msgHeader.length);
	    		pstmt.executeUpdate();
        	} catch(SQLException ex) {
        		System.out.println("Encountered an error uploading new header row!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    	}
    }
    
    public void uploadBody(MessageBody msgBody, MessageHeader msgHeader) {
    	this.connect();
    	if (this.isConnected()) {
    		try {
    			this.uploadStruct(msgBody,msgHeader);
        	} catch(SQLException ex) {
        		System.out.println("Encountered an error uploading new struct row!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    	}
    }
    
    private void uploadStruct(MessageBody msgBody, MessageHeader msgHeader) throws SQLException {
    	this.connect();
    	if (this.isConnected()) {
    		try {
    			String query = "INSERT INTO \"struct#" + msgBody.msgCode + "\"(ser_num,";
    			String vals = "(" + msgHeader.serialNumber + ",";
    			for (int i = 0; i < msgBody.values.size(); i++) {
    				MessageValue msgVal = msgBody.values.get(i);
    				query += "\"" + msgVal.fieldName + ":" + msgVal.getType() + "\""; // col name
    				if (i < msgBody.values.size() - 1) {
    					vals += "?,";
    					query += ",";
    				}
    				else {
    					vals += "?)";
    					query += ") VALUES" + vals;
    				}
    			}
	    		PreparedStatement pstmt = conn.prepareStatement(query);
	    		for(int i = 1; i <= msgBody.values.size(); i++) {
	    			MessageValue msgVal = msgBody.values.get(i - 1);
	    			msgVal.addValueQuery(pstmt, i);
	    		}
	    		pstmt.executeUpdate();
        	} catch(SQLException ex) {
        		System.out.println("Encountered an error uploading new struct row!");
                System.out.println(ex.getMessage());
                Logger.logError(ex.toString());
        	}
    	}
    }
}
