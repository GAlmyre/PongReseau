package pong;
import pong.gui.Pong;

import java.awt.Image;
import java.awt.Point;

public class Ball extends PongItem {

	/* Gives the direction of the ball */
	private Point speed;
	
	/* Getters and setters */
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

	/* Default constructor */
	public Ball() {
		super();
		this.speed = new Point(0, 0);
	}
	
	/* Constructor with parameters */
	public Ball(Point position, Image sprite, int width, int height, Point speed) {
		super(position, sprite, width, height);
		this.speed = (Point) speed.clone();
	}

	/* Ball's movement */
	@Override
	public void Move() {

		this.position.translate(this.speed.x, this.speed.y);
	}

	/* Handles the collision with the rackets */
	public void Collide (Racket r1, Racket r2, int pongX, int pongY) {

		/**
		 *  Check collision with the rackets
		 */
		Racket rG;
		Racket rD;

		if(r1.getPosition().x < r2.getPosition().x) {
			rG = r1;
			rD = r2;
		}
		else{
			rG = r2;
			rD = r1;
		}

		/* Do we hit the first racket ? */
		if (this.getPosition().x < rG.getPosition().x+rG.getWidth()
				&& this.getPosition().y <= rG.getPosition().y+rG.getHeight()/2
				&& this.getPosition().y >= rG.getPosition().y)
		{
			/* change trajectory */
			this.setX(rG.getPosition().x+ rG.getWidth());
			this.setSpeedX(-this.getSpeed().x);
		}
		/* Do we hit the second racket ? */
		if (this.getPosition().x + this.getWidth() > rD.getPosition().x
				&& this.getPosition().y <= rD.getPosition().y+rD.getHeight()/2
				&& this.getPosition().y >= rD.getPosition().y)
		{
			/* Change trajectory */
			this.setX(rD.getPosition().x - this.getWidth());
			this.setSpeedX(-this.getSpeed().x);
		}

		/**
		 * Collision with the walls and score update
		 */
		
		if (this.getPosition().x < 0)
		{
			this.setX(0);
			this.setSpeedX(-this.getSpeed().x);
			Pong.updateScore(false); // On point for Player 1
			this.setPosition(new Point(pongX/2,pongY/2));
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
			Pong.updateScore(true); // one point for Player 2
			this.setPosition(new Point(pongX/2,pongY/2));
		}
		if (this.getPosition().y > pongY - this.getHeight())
		{
			this.setY(pongY - this.getHeight());
			this.setSpeedY(-this.getSpeed().y);
		}

	}
}
