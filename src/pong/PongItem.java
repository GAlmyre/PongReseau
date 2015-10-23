package pong;

import java.awt.Image;
import java.awt.Point;

public abstract class PongItem {
	
	/* Classe abstraite dont hériteront tous les objets visuels du jeu */
	
	/* Position du point */
	protected Point position;
	
	/* Image de l'objet */
	protected Image sprite;
	
	/* Dimensions */
	protected int width;
	protected int height;
	
	/* Accesseurs */
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setX(int x) {
		this.position.x = x;
	}

	public void setY(int y) {
		this.position.y = y;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/* Constructeur par défaut */
	public PongItem() {
		this.position = new Point(0,0);
		this.sprite = null;
		this.width = 0;
		this.height = 0;
	}
	
	/* Constructeur avec paramètres */
	public PongItem(Point position, Image sprite, int width, int height) {
		this.position = position;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	}
	/* Déplacement à implémenter dans les descendants */
	public abstract void Move();
}
