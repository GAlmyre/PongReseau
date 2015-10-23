package pong;
import java.awt.Image;
import java.awt.Point;

public class Ball extends PongItem {

	/* Point pour indiquer la direction de la balle */
	private Point speed;
	
	/* Accesseurs */
	public Point getSpeed() {
		return speed;
	}

	public void setSpeed(Point speed) {
		this.speed = speed;
	}

	/* Constructeur par défaut */
	public Ball() {
		super();
		this.speed = new Point(0, 0);
	}
	
	/* Constructeur avec paramètres */
	public Ball(Point position, Image sprite, int width, int height, Point speed) {
		super(position, sprite, width, height);
		this.speed = (Point) speed.clone();
	}

	/* Déplacement de la balle */
	@Override
	public void Move() {
		this.position.x += speed.x;
		this.position.y += speed.y;
	}
}
