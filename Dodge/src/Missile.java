import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * @author Raymond Guo
 *
 * Missile class
 */
public class Missile extends Sprite {

	private boolean missileDeployed; //is missile put on screen?
	public static Image IMG_MISSILE1; //image used for missiles 1
	public static Image IMG_MISSILE2; //image used for missiles 2
	public static Image IMG_MISSILE3; //image used for missiles 3
	private int currentColor=0;

	/**
	 * tests and reads image file for missiles
	**/
	static {
		try {
			IMG_MISSILE1=ImageIO.read(new File("Missile_IMAGE1.png"));
		} catch (IOException e) {
			System.out.println("Missile Image Failed");
		}
		
		try {
			IMG_MISSILE2=ImageIO.read(new File("Missile_IMAGE2.png"));
		} catch (IOException e) {
			System.out.println("Missile Image Failed");
		}
		
		try {
			IMG_MISSILE3=ImageIO.read(new File("Missile_IMAGE3.png"));
		} catch (IOException e) {
			System.out.println("Missile Image Failed");
		}
	}

	/**
	 * Returns if a missile has been put on screen
	 * 
	 * @return if missile is deployed
	**/
	public boolean getMissileDeployed() {
		return missileDeployed;
	}

	/**
	 * Sets a missile to deployed (or not deployed)
	 * 
	 * @param deployedOrNot - if missile is now deployed
	**/
	public void setMissileDeployed(boolean deployedOrNot) {
		this.missileDeployed = deployedOrNot;
	}
	
	/**
	 * Returns the answer the missile represents
	 * 
	 * @return current color of missile
	**/
	public int getCurrentColor() {
		return currentColor;
	}

	/**
	 * Constructor
	 * 
	 * @param posX - x position of missile
	 * @param posY - y position of missile
	 * @param sizeX - width of missile
	 * @param sizeY - height of missile
	 * @param speedY - rate at which missile falls
	**/
	public Missile(int posX, int posY, int sizeX, int sizeY, int speedY){
		super(); //call default constructor
		setPosX(posX); //set fields
		setPosY(posY);
		setSizeX(sizeX);
		setSizeY(sizeY);
		setSpeedY(speedY);
		setMissileDeployed(false);
	}

	/**
	 * Draws missiles
	 * 
	 * @param g a graphics object of the second dimension
	**/
	@Override
	public void draw(Graphics2D g) {
		if (currentColor==0) { //if missile color has not yet been set
			Random rand = new Random();
			int  n = rand.nextInt(3) + 1;
			
			if (n==1) {
				g.drawImage(IMG_MISSILE1, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
				currentColor = 1;
			}
			else if (n==2){
				g.drawImage(IMG_MISSILE2, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
				currentColor = 2;
			}
			else {
				g.drawImage(IMG_MISSILE3, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
				currentColor = 3;
			}
		} else { //otherwise keep the same color
			if (currentColor==1) {
				g.drawImage(IMG_MISSILE1, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
			}
			else if (currentColor==2){
				g.drawImage(IMG_MISSILE2, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
			}
			else {
				g.drawImage(IMG_MISSILE3, getPosX(), getPosY(), getSizeX(), getSizeY(), null);
			}
		}
	}
}
