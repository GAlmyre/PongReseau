package pong;

import pong.gui.Window;
import pong.gui.Pong;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starting point of the Pong application
 */


public class Main  {

	public static void main(String[] args) throws IOException {
		if(args.length>0) {
			Pong pong = new Pong(args[0]);
		}
		else {
			Pong pong = new Pong(null);
		}
		Window window = new Window(pong);
		window.displayOnscreen();
	}
}
