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

public class Remote extends Thread {
	private static String IPaddress = "192.168.0.7";
	private static int port = 1234;
	public static Socket connection = new Socket();
	public static DataInputStream dis;
	public static DataOutputStream dos;
	private static int MAX_READ = 30;
	private static BufferedInputStream in = null;
	private static OutputStream out = null;
	private static String input = "";
	
	public static void setInput() {
		input = "";
	}
	
	public static String getInput() {
		return input;
		
	}
	
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
				in = new BufferedInputStream( connection.getInputStream());
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
					LCD.drawString("Chars read: ", 0, 2);
					LCD.drawInt(in.available(), 12, 2);
					int read = in.read(buffer, 0, MAX_READ);
					for (int index= 0 ; index < read ; index++) {						
						input = input + (char)buffer[index];
					}
					out.write("Reply:".getBytes(), 0, 6);
					out.write(buffer, 0, read);
				}
			} catch (IOException ignored) {
			}
		}
    }
}
