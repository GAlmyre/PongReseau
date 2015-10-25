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
	 * One Racket to be displayed
	 */
	private final Racket racket;
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

	public Pong() {
		ImageIcon icon;

		Image tmpImage = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("image/ball.png"));
		
		/* création de la balle */
		ball = new Ball(ball_position, tmpImage, ball_width, ball_height, ball_speed);
		
		icon = new ImageIcon(ball.getSprite());
		ball.setHeight(icon.getIconHeight());
		ball.setWidth(icon.getIconWidth());
		
		/* création de la raquette */
		tmpImage = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("image/racket.png"));
		racket = new Racket(racket_position,tmpImage, racket_width, racket_height, racket_speed);
		
		icon = new ImageIcon(racket.getSprite());
		racket.setHeight(icon.getIconHeight());
		racket.setWidth(icon.getIconWidth());

		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
		
	}

	/**
         * Proceeds to the movement of the ball and updates the screen
	 */
	public void animate() {
		/* Update ball position */
		ball.Move();

		/* border collisions with ball  */
		rebond(ball);

		/* when the ball meets a racket */
		checkCollide(racket,ball);

		/* Update racket position */
		racket.Move();
		
		if (racket.getPosition().y < 0)
			racket.setY(0);
		if (racket.getPosition().y > SIZE_PONG_Y - racket.getHeight()/2)
			racket.setY(SIZE_PONG_Y - racket.getHeight()/2);

		/* And update output */
		updateScreen();
	}

	public void checkCollide(Racket racket, Ball ball) {

		if (ball.getPosition().x < racket.getPosition().x+racket.getWidth() && ball.getPosition().y <= racket.getPosition().y+racket.getHeight()/2)
		{
			ball.setX(racket.getPosition().x+racket.getWidth());
			ball.setSpeedX(-ball.getSpeed().x);
		}

	}

	public void rebond(Ball ball) {

		if (ball.getPosition().x < 0)
		{
			ball.setX(0);
			ball.setSpeedX(-ball.getSpeed().x);
		}
		if (ball.getPosition().y < 0)
		{
			ball.setY(0);
			ball.setSpeedY(-ball.getSpeed().y);
		}
		if (ball.getPosition().x > SIZE_PONG_X - ball.getWidth())
		{
			ball.setX(SIZE_PONG_X - ball.getWidth());
			ball.setSpeedX(-ball.getSpeed().x);
		}
		if (ball.getPosition().y > SIZE_PONG_Y - ball.getHeight())
		{
			ball.setY(SIZE_PONG_Y - ball.getHeight());
			ball.setSpeedY(-ball.getSpeed().y);
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				racket.setSpeed(-RACKET_SPEED);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				racket.setSpeed(RACKET_SPEED);
				break;
			default:
				System.out.println("got press "+e);
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				racket.setSpeed(0);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				racket.setSpeed(0);
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
	public void updateScreen() {
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_PONG_X, SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		/* Fill the area with blue */
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y);

		/* Draw items */
		graphicContext.drawImage(ball.getSprite(), ball.getPosition().x, ball.getPosition().y, ball.getWidth(), ball.getHeight(), null);
		graphicContext.drawImage(racket.getSprite(), racket.getPosition().x, racket.getPosition().y, racket.getWidth(), racket.getHeight(), null);

		this.repaint();
	}
}
