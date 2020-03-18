package uk.mrinterbugs.aedan;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import lejos.hardware.lcd.LCD;

/**
 * This class is the ground work for connecting to the android app.
 * Provided by @author cyclingProfessor
 * We have threaded this so that we can run other behaviours at the same time.
 *
 * @author Aedan Lawrence
 * @author Bruce Lay
 * @author Edmund Chee
 * @author Jules James
 * 
 * @version 0.5
 * @since 2020-02-27
 */
public class AndroidConnection extends Thread {
	private static String IPaddress = "192.168.0.7";
	private static int port = 1234;
	public static Socket connection = new Socket();
	public static DataInputStream dis;
	public static DataOutputStream dos;
	private static int MAX_READ = 30;
	private static BufferedInputStream in = null;
	private static OutputStream out = null;
	private static String input = "";
	
	/**
	 * Setter used to clear the value of input once it has been handled by a behaviour.
	 * @throws InterruptedException 
	 */
	public synchronized void setInput(String input) throws InterruptedException {
		AndroidConnection.input = input;
		this.wait();
	}
	
	/**
	 * Allows behaviours to get the input from the android app.
	 * @throws InterruptedException 
	 */
	public synchronized String getInput() throws InterruptedException {
		this.notify();
		String current = input;
		input = "";
		return current;
	}
	
	/**
	 * This is the thread that allows the connection to the app and generates the string from an input.
	 */
    public void run() {
    	byte[] buffer = new byte[MAX_READ];
		
		SocketAddress sa = new InetSocketAddress(IPaddress, port);
		try {
			connection.connect(sa, 1500);
		} catch (Exception ex) {
			LCD.drawString(ex.getMessage(), 0,6);
			connection = null;
		}
		if (connection != null) {
			try {
				in = new BufferedInputStream(connection.getInputStream());
			} catch (IOException ignored) {
			}
			try {
				out = connection.getOutputStream();
			} catch (IOException ignored) {
			}
		}

		while (connection != null) {
			try {
				if (in.available() > 0) {
					input = "";
					int read = in.read(buffer, 0, MAX_READ);
					for (int index= 0 ; index < read ; index++) {						
						input = input + (char)buffer[index];
					}
					LCD.drawString(input, 0, 2);
					out.write("Reply:".getBytes(), 0, 6);
					out.write(buffer, 0, read);
					try {
						setInput(input);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println(e);
					}
				}
			} catch (IOException ignored) {
			}
		}
    }
}
