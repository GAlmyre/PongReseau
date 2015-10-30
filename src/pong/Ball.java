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

	public void setSpeedX(int speedX) {

		this.speed.x = speedX;
	}

	public void setSpeedY(int speedY) {

		this.speed.y = speedY;
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

		this.position.translate(this.speed.x, this.speed.y);
	}

	/* Gestion des collisions avec les raquettes */
	public void Collide (Racket rG, Racket rD, int pongX, int pongY) {

		/**
		 *  Verification des collisions avec les raquettes
		 */

		/* On vérifie que l'on touche la première raquette */
		if (this.getPosition().x < rG.getPosition().x+rG.getWidth()
				&& this.getPosition().y <= rG.getPosition().y+rG.getHeight()/2
				&& this.getPosition().y >= rG.getPosition().y)
		{
			/* On modifie la trajectoire */
			this.setX(rG.getPosition().x+ rG.getWidth());
			this.setSpeedX(-this.getSpeed().x);
		}
		/* On vérifie que l'on touche la deuxième raquette */
		if (this.getPosition().x + this.getWidth() > rD.getPosition().x
				&& this.getPosition().y <= rD.getPosition().y+rD.getHeight()/2
				&& this.getPosition().y >= rG.getPosition().y)
		{
			/* On modifie la trajectoire */
			this.setX(rD.getPosition().x - this.getWidth());
			this.setSpeedX(-this.getSpeed().x);
		}

		/**
		 * Verification des collisions avec les murs
		 */
		
		if (this.getPosition().x < 0)
		{
			this.setX(0);
			this.setSpeedX(-this.getSpeed().x);
		}
		if (this.getPosition().y < 0)
		{
			this.setY(0);
			this.setSpeedY(-this.getSpeed().y);
		}
		if (this.getPosition().x > pongX - this.getWidth())
		{
			this.setX(pongX - this.getWidth());
			this.setSpeedX(-this.getSpeed().x);
		}
		if (this.getPosition().y > pongY - this.getHeight())
		{
			this.setY(pongY - this.getHeight());
			this.setSpeedY(-this.getSpeed().y);
		}

	}
}
