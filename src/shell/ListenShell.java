/*
 * This shell is used for listening for messages from LMDS, 
 * then translating and uploading to DB.
 * "exit" command exits shell, else keeps listening.
 * 
 */

package shell;
import java.util.Scanner;
import serial_comm.Port;

public class ListenShell {
	public static final int SECONDS_RUN = 30;
	
	public static void main(String[] args) {
		Port comPort = new Port();
		Scanner in = new Scanner(System.in);
		boolean exit = false;
		
		comPort.open();
		comPort.startListen();
		System.out.print("Started listening...");
		
		while (!exit) {
			String input;
			System.out.print("#Gopher: ");
			input = in.nextLine();
			switch(input) {
				case "exit":
					System.out.println("Exiting...");
					exit = true;
					break;
				default:
					System.out.println("Unknown command, try again!");
					break;
			}
		}
		in.close();
		comPort.stopListen();
		comPort.close();
		System.exit(0);
	}
}
