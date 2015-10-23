package pong;

import java.awt.Image;
import java.awt.Point;

public class Racket extends PongItem {
	
	private int speed;
	
	/* Accesseurs */
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/* Constructeur par défaut */
	public Racket() {
		super();
		this.speed = 0;
	}

	/* Constructeur avec arguments */
	public Racket(Point position, Image sprite, int width, int height, int speed) {
		super(position, sprite, width, height);
		this.speed = speed;
	}

	/* Déplacement de la raquette */
	@Override
	public void Move() {
		this.position.y += this.speed;
	}

}
