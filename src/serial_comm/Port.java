package serial_comm;

import com.fazecast.jSerialComm.SerialPort;

import messages.Message;



/**
 * @author Gil
 *
 */
public class Port {
	public SerialPort comPort; // 1 is just for demonstration
	private MessageListener listener;
	public boolean isOpen, isListening;
	
	public Port() {
		this.comPort = SerialPort.getCommPorts()[0];
		this.listener = null;
		this.isListening = false;
		this.isOpen = false;
	}
	
	public void open() {
		this.comPort.openPort();
		comPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 2000, 2000);
		this.isOpen = true;
	}
	
	public void close() {
		this.comPort.closePort();
		this.isOpen = false;
	}
	
	public void startListen() {
		this.listener = new MessageListener();
        this.comPort.addDataListener(listener);
        this.isListening = true;
	}
	
	public void stopListen() {
		this.comPort.removeDataListener();
		this.isListening = false;
	}
	
	public void sendMessageBytes(Message msg) {
		if (!this.isOpen )
			return;
		System.out.println("Sending message...");
		int result = this.comPort.writeBytes(msg.getBytes(), msg.getBytes().length);
		System.out.println("END BYTES: " + msg.getBytes()[msg.getBytes().length -1] + msg.getBytes()[msg.getBytes().length - 2]);
		System.out.println("Finished: "+result);
	}
	
	@Override
	public String toString() {
		return this.comPort.getDescriptivePortName();
	}
}
