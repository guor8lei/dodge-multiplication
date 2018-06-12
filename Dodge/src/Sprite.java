import java.awt.Graphics2D;

/**
 * Template for moving objects
 * @author Danny Navarro
 */
public abstract class Sprite {
	private int posX,posY; //position on screen
	private int sizeX,sizeY; //size on screen
	private int speedX,speedY; //speed pixels per frame
	
	/**
	 * @return  pixels per frame,y
	 */
	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	
	/**
	 * Draw yourself
	 * @param g graphics object
	 */
	public abstract void draw(Graphics2D g);

	/**
	 * @return x coord of top left corner
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @param newTopX x coord of top left corner
	 */
	public void setPosX(int newTopX) {
		posX = newTopX;
	}

	/**
	 * @return y coord of top left corner
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param  topY y coord of top left corner
	 */
	public void setPosY(int topY) {
		posY = topY;
	}

	/**
	 * 
	 * @return size in x dimension
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * 
	 * @param xSize size in x dimension
	 */
	public void setSizeX(int xSize) {
		sizeX = xSize;
	}

	/**
	 * 
	 * @return size in y dimension
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * 
	 * @param ySize size in y dimension
	 */
	public void setSizeY(int ySize) {
		sizeY = ySize;
	}
}
