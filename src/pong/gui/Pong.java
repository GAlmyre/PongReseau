package pong.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pong.Ball;
import pong.PongItem;
import pong.Racket;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */


/*
	Pour la partie réseau
	On crée un socket dans le programme en fonction des arguments
	si argument il y a, ce sera le string du constructeur du socket, sinon ce sera la machine "principale" et on utilisera donc localhost
 */
public class Pong extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(0xFF, 0x40, 0);

	/**
	 * Width of pong area
	 */
	private static final int SIZE_PONG_X = 800;
	/**
	 * Height of pong area
	 */
	private static final int SIZE_PONG_Y = 600;
	/**
	 * Time step of the simulation (in ms)
	 */
	public static final int timestep = 10;
	/**
	 * Speed of ball (in pixels per second)
	 */
	public static final int BALL_SPEED = 2;
	/**
	 * Speed of racket (in pixels per second)
	 */
	public static final int RACKET_SPEED = 4;

	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

	/**
	 * Ball to be displayed
	 */
	private final Ball ball;
	/**
	 * Width of ball in pixels
	 */
	private int ball_width;
	/**
	 * Height of ball in pixels
	 */
	private int ball_height;
	/**
	 * Position of ball
	 */
	private Point ball_position = new Point(0, 0);
	/**
	 * Speed of ball, in pixels per timestep
	 */
	private Point ball_speed = new Point(BALL_SPEED, BALL_SPEED);

	/**
	 * The Rackets to be displayed
	 */
	private final Racket MyRacket;
	private final Racket P2Racket;
	/**
	 * Width of the racket in pixels
	 */
	private int racket_width;
	/**
	 * Height of the racket in pixels
	 */
	private int racket_height;
	/**
	 * Speed of racket, in pixels per timestamp
	 */
	private int racket_speed;
	/**
	 * Position of racket
	 */
	private Point racket_position = new Point(0, 0);

	/**
	 * Sockets et streams
	 */
	private ServerSocket server;
	private Socket client;
	private OutputStream os;
	private InputStream is;
	private boolean isServer;
	private ImageIcon icon;

	/* Initialisation des objets du Pong */
	public Pong(String[] args) throws IOException {
		ImageIcon icon;

		Image tmpImage = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("image/ball.png"));
		
		/* création de la balle */
		ball = new Ball(ball_position, tmpImage, ball_width, ball_height, ball_speed);
		
		icon = new ImageIcon(ball.getSprite());
		ball.setHeight(icon.getIconHeight());
		ball.setWidth(icon.getIconWidth());
		
		/* création des raquettes */
		tmpImage = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("image/racket.png"));
		MyRacket = new Racket(racket_position,tmpImage, racket_width, racket_height, racket_speed);
		P2Racket = new Racket(racket_position,tmpImage, racket_width, racket_height, racket_speed);
		
		icon = new ImageIcon(MyRacket.getSprite());
		MyRacket.setHeight(icon.getIconHeight());
		MyRacket.setWidth(icon.getIconWidth());

		icon = new ImageIcon(P2Racket.getSprite());
		P2Racket.setHeight(icon.getIconHeight());
		P2Racket.setWidth(icon.getIconWidth());

		InitSockets(args);

		if(isServer) {
			P2Racket.setPosition(new Point(SIZE_PONG_X - P2Racket.getWidth(), SIZE_PONG_Y/2));
		}
		else {
			MyRacket.setPosition(new Point(SIZE_PONG_X - P2Racket.getWidth(), SIZE_PONG_Y/2));
		}

		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
	}

	/**
         * Proceeds to the movement of the ball and updates the screen
	 */
	public void animate() throws IOException {
		/* Update ball position */
		ball.Move();

		/* collisions de la balle */
		ball.Collide(MyRacket, P2Racket, SIZE_PONG_X, SIZE_PONG_Y);

		/* Update racket position */
		MyRacket.Move();

		/* Collisions des raquettes */
		MyRacket.Collide(SIZE_PONG_Y);
		P2Racket.Collide(SIZE_PONG_Y);

		/* And update output */
		updateScreen();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				MyRacket.setSpeed(-RACKET_SPEED);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				MyRacket.setSpeed(RACKET_SPEED);
				break;
			default:
				System.out.println("got press "+e);
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				MyRacket.setSpeed(0);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				MyRacket.setSpeed(0);
				break;
			default:
				System.out.println("got release "+e);
		}
	}
	public void keyTyped(KeyEvent e) { }

	/*
	 * (non-Javadoc) This method is called by the AWT Engine to paint what
	 * appears in the screen. The AWT engine calls the paint method every time
	 * the operative system reports that the canvas has to be painted. When the
	 * window is created for the first time paint is called. The paint method is
	 * also called if we minimize and after we maximize the window and if we
	 * change the size of the window with the mouse.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 * Draw each Pong item based on new positions
	 */
	public void updateScreen() throws IOException {
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_PONG_X, SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		transfert();
		/* Fill the area with blue */
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y);

		/* Draw items */
		graphicContext.drawImage(ball.getSprite(), ball.getPosition().x, ball.getPosition().y, ball.getWidth(), ball.getHeight(), null);
		graphicContext.drawImage(MyRacket.getSprite(), MyRacket.getPosition().x, MyRacket.getPosition().y, MyRacket.getWidth(), MyRacket.getHeight(), null);
		graphicContext.drawImage(P2Racket.getSprite(), P2Racket.getPosition().x, P2Racket.getPosition().y, P2Racket.getWidth(), P2Racket.getHeight(), null);
		this.repaint();
	}

	/**
	 * Méthode de transfert de données d'un Pong à l'autre
	 * @throws IOException
	 */
	public void transfert() throws IOException {

		// transfert d'infos par les sockets
		os = client.getOutputStream();
		is = client.getInputStream();
		DataOutputStream out = new DataOutputStream(os);
		DataInputStream in = new DataInputStream(is);
		if (!isServer) {
			out.writeInt(MyRacket.getPosition().y);
		}
		if (isServer) {
			out.writeInt(MyRacket.getPosition().y);
		}
		P2Racket.setY(in.readInt());
	}

	/**
	 * Méthode d'initialisation des sockets, client, et serveur si il y a lieu
	 * @param args
	 * @throws IOException
	 */
	public void InitSockets(String[] args) throws IOException {

		try {
			if(args.length == 0) {
				server = new ServerSocket(1844);
				isServer = true;
				System.out.println("waiting ...");
				if(isServer)
					client = server.accept();
				System.out.println("connected!");
			}
			else {
				client = new Socket(args[0], 1844);
				isServer = false;
			}
		}
		catch(Exception e) {
			// traitement d'erreur
		}
	}
}
