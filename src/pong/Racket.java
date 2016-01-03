package pong;

import java.awt.Image;
import java.awt.Point;

public class Racket extends PongItem {
	
	private int speed;
	
	/* Getters and setters */
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/* Default constructor */
	public Racket() {
		super();
		this.speed = 0;
	}

	/* Constructor with parameters */
	public Racket(Point position, Image sprite, int width, int height, int speed) {
		super(position, sprite, width, height);
		this.speed = speed;
	}

	/* RAcket movement */
	@Override
	public void Move() {
		this.position.y += this.speed;
	}

	/* Collision between racket and walls */
	public void Collide(int pongY) {

		if (this.getPosition().y < 0)
			this.setY(0);
		if (this.getPosition().y > pongY - this.getHeight()/2)
			this.setY(pongY - this.getHeight()/2);

	}

	public void setY(int y) {
		this.position.y = y;
	}
}
