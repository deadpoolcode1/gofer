/*
 * This shell is used for reading messages from TestShell, sending them to proper COM Port 
 * and updating message to READ=1 in DB.
 * "read" command to check for unread messages/
 * "exit" command to close shell.
 */


package shell;

import db.*;
import messages.Message;
import messages.MessageHeader;
import serial_comm.Port;

import java.util.ArrayList;
import java.util.Scanner;

public class MainShell {

	public static void main(String[] args) {
		Port comPort = new Port();
		DataBase db = new DataBase();
		Scanner in = new Scanner(System.in);
		boolean exit = false;
		System.out.println("Starting shell...");
		
		while (!exit) {
			String input;
			System.out.print("#Gopher: ");
			input = in.nextLine();
			switch(input) {
				case "exit":
					System.out.println("Exiting...");
					exit = true;
					break;
				case "read":
					System.out.println("Reading new messages...");
					db.connect();
					comPort.open();
					System.out.println("Opened Port: " + comPort);
					ArrayList<MessageHeader> headerList = db.getUnreadHeaders();
					for(MessageHeader dbHeader: headerList) {
						Message msg = new Message(db.getBody(dbHeader), dbHeader);
						msg.sendMessage();
						comPort.sendMessageBytes(msg);
						try { Thread.sleep(1000 * 2); } catch (Exception e) { e.printStackTrace(); }
					}
					db.updateToRead();
					db.disconnect();
					comPort.close();
					break;
				default:
					System.out.println("Unknown command, try again!");
					break;
			}
		}
		in.close();
		System.exit(0);
	}

}
