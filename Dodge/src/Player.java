import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author Raymond Guo
 *
 * Player class
 */
public class Player extends Sprite {

	public int score; //player score
	public static Image IMG_PLAYER; //image that is used for player sprite
	
	/**
	 * tests and reads image file for missiles
	**/
	static {
		try {
			IMG_PLAYER=ImageIO.read(new File("Player_IMAGE.gif"));
		} catch (IOException e) {
			System.out.println("Player Image Failed");
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param posX - x position of player
	 * @param posY - y position of player
	 * @param sizeX - width of player
	 * @param sizeY - height of player
	 * @param speedX - rate at which player moves left and right
	**/
	public Player(int posX, int posY, int sizeX, int sizeY, int speedX){
		super(); //call default constructor
		setPosX(posX); //set fields
		setPosY(posY);
		setSizeX(sizeX);
		setSizeY(sizeY);
		setSpeedX(speedX);
	}

	/**
	 * Draws player
	 * 
	 * @param g a graphics object of the second dimension
	**/
	@Override
	public void draw(Graphics2D g) {
			g.drawImage(IMG_PLAYER, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
		}
	
	/**
	 * Makes the player move left or right
	 * 
	 * @param moveLeftOrRight - which direction to move (true = left, false = right)
	**/	 
	public void doMove(boolean moveLeftOrRight){
		 
			if(moveLeftOrRight==true) { 
				setPosX(getPosX()-getSpeedX()); //move left
			} else {
				setPosX(getPosX()+getSpeedX()); //move right
			}
		 
	}

	/**
	 * Set to a new score
	 * 
	 * @param score - new score
	**/	 
	public void setScore(int score) {
			this.score = score; 
	}	
	
	/**
	 * Increments score
	**/
	public void increment(){
			this.score++; //increments the score by 1
	}
}
