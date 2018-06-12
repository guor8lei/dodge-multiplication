import java.awt.Color;
import java.net.MalformedURLException;
import java.util.Random;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author Raymond Guo
 *
 * Multiplication game class
 */
public class MultiplicationGame extends GameTemplate {
	
	private Player myPlayer; //initialize Player
	
	/*
	 * Constants
	 */
	public static final int NUM_MISSILES=1000;
	private Missile[] myMissiles= new Missile[NUM_MISSILES];
	
	public static int SPEED_X_PLAYER=7; //changes based on difficulty
	public static final int SIZE_Y_PLAYER=50;
	public static final int SIZE_X_PLAYER=50;
	
	public static int SPEED_Y_MISSILE=5; //changes based on difficulty
	public static final int SIZE_Y_MISSILE=50;
	public static final int SIZE_X_MISSILE=6;
	
	/*
	 * State Values
	 */
	public static final int STATE_BEFORE=0; //main menu
	public static final int STATE_PLAYING=1; //game playing
	public static final int STATE_DONE=2; //game over
	public static final int STATE_QUIT=3; //exit screen
	public static final int STATE_INSTRUCT=4; //instruction screen
	public static final int STATE_LEVEL=5; //change difficulty screen
	
	public static final String FONT_TYPE="Verdana Italic"; //font
	
	/*
	 * State Variables
	 */
	private int gameState; //current state
	private int frameCount; //Initiate frameCount to 0
	
	public int missleCounter=0;
	public int time=0;
	public String currentQuestion="";
	public String currentChoices="";
	public int currentAnswer=0;
	public int correctChoice=0;
	
	// Image Loads
	private static Image BG_SPACE;
	private static Image BG_QUIT;

	//Reads the background image the first time only
	static {
		try {
			BG_SPACE = ImageIO.read(new File("starry_Background1.jpg"));
		} catch (IOException e) {
			System.out.println("No background");
		}
	}
	
	//Reads the quit screen image
	static {
		try {
			BG_QUIT = ImageIO.read(new File("Dodge.png"));
		} catch (IOException e) {
			System.out.println("No quit pic");
		}
	}
	
	/**
	 * Default Constructor
	**/
	public MultiplicationGame(){
		super(); //call super constructor
	} 
		
	/**
	 * Initializes the game objects 
	 * Gets ready to begin the game
	**/
	@Override
	public void init() {
		myPlayer=new Player(0,0,SIZE_X_PLAYER,SIZE_Y_PLAYER,SPEED_X_PLAYER);
		for (int i=0;i<myMissiles.length;i++) {
			myMissiles[i]=new Missile(0,0,SIZE_X_MISSILE,SIZE_Y_MISSILE,SPEED_Y_MISSILE);
		}
		gameState=STATE_BEFORE;
		frameCount=0;
		time=0;
	}
	
	/**
	 * Used to re-initialize at the end of the round
	 * Key difference is the setting of the missiles to not deploy
	**/
	public void initAgain() {
		myPlayer.setSpeedX(SPEED_X_PLAYER);
		for (int i=0;i<myMissiles.length;i++) {
			myMissiles[i].setSpeedY(SPEED_Y_MISSILE);
		}
		gameState=STATE_BEFORE;
		frameCount=0;
		for (int i=0;i<myMissiles.length;i++) {
			myMissiles[i].setMissileDeployed(false);
		}
		missleCounter=0;
		time=0;
	}

	/**
	 * Starts the game and generates information necessary for the board
	**/
	@Override
	public void start() {
		if (gameState==STATE_PLAYING) {
			myPlayer.setScore(0);
			myPlayer.setPosX(getWidth()/2);
			myPlayer.setPosY(getHeight()*4/5);
		}
	}

	/**
	 * Updates the game frame by frame
	 * 
	 * @param g a graphics object of the second dimension
	**/
	@Override
	public void updateFrame(Graphics2D g){ 
		switch(gameState){
		
		case STATE_BEFORE: //main menu
			drawBoard(g);
			drawStartScreen(g);
			//User inputs
			if(isAKeyDown(KeyEvent.VK_SPACE)) gameState = STATE_PLAYING;
			if(isAKeyDown(KeyEvent.VK_I)) gameState = STATE_INSTRUCT;
			if(isAKeyDown(KeyEvent.VK_Q)) gameState = STATE_QUIT;
			if(isAKeyDown(KeyEvent.VK_D)) gameState = STATE_LEVEL;
			start();
			break;
			
		case STATE_PLAYING: //game playing
			if (time==0) changeQuestion();
			time++;
			myPlayer.setPosY(getHeight()-75);
			frameCount++;
			//Deploy missiles
			if (missleCounter<myMissiles.length) {
				if (frameCount>(int)(Math.random() * 50)) {
					myMissiles[missleCounter].setMissileDeployed(true);
					myMissiles[missleCounter].setPosY(0);
					myMissiles[missleCounter].setPosX((int)(1+(Math.random() * ((getWidth())-myMissiles[missleCounter].getSizeX() + 1)+1)));	
					frameCount=0;
					missleCounter++;
				}
			}
			
			if(isAKeyDown(KeyEvent.VK_LEFT) && canMove(myPlayer,true)) myPlayer.doMove(true);
			else if(isAKeyDown(KeyEvent.VK_RIGHT) && canMove(myPlayer,false)) myPlayer.doMove(false);

			//Resets missile position
			for (int i=0;i<myMissiles.length;i++) {
				if (myMissiles[i].getMissileDeployed()==true) {
					myMissiles[i].setPosY(myMissiles[i].getPosY()+myMissiles[i].getSpeedY());
					if (myMissiles[i].getPosY()>getHeight()) {
						myMissiles[i].setPosY(0);
						myMissiles[i].setPosX((int)(Math.random() * ((getWidth())-myMissiles[i].getSizeX() + 1)));
					}
				}
				
				//Collision detection
				if (0<=myPlayer.getPosY()-myMissiles[i].getPosY() && myPlayer.getPosY()-myMissiles[i].getPosY()<=myMissiles[i].getSizeY()) {
					if ((myPlayer.getPosX()<myMissiles[i].getPosX() && (-myPlayer.getSizeX()<=myPlayer.getPosX()-myMissiles[i].getPosX()) && (myPlayer.getPosX()-myMissiles[i].getPosX()<myMissiles[i].getSizeX())) || (myPlayer.getPosX()>myMissiles[i].getPosX() && (0<=myPlayer.getPosX()-myMissiles[i].getPosX()) && (myPlayer.getPosX()-myMissiles[i].getPosX()<myMissiles[i].getSizeX()))) {
						if (myMissiles[i].getCurrentColor()==correctChoice) myPlayer.increment();
						else {
							gameState = STATE_DONE;
						}
					}		
				}
			}
			
			if (time/500.0==(int)(time/500)) changeQuestion();
		
			//Draw things on screen
			drawBoard(g);
			drawSprites(g);
			drawScore(g);
			drawQuestion(g);
			break;

		case STATE_DONE: //game over
			drawBoard(g);
			drawEndScreen(g);
			//User input
			if(isAKeyDown(KeyEvent.VK_N)){
				gameState = STATE_QUIT;
				
			}
			if(isAKeyDown(KeyEvent.VK_Y)){
				init(); 
			}
			break;
			
		case STATE_QUIT: //quit game
			drawQuitScreen(g);
	        break;
	        
		case STATE_INSTRUCT: //instruction screen
			drawInstructionScreen(g);
			if(isAKeyDown(KeyEvent.VK_BACK_SPACE)) gameState = STATE_BEFORE;
			break;
			
		case STATE_LEVEL: //difficulty change
			drawLevelScreen(g);
			//User input
			if(isAKeyDown(KeyEvent.VK_1)) {
				SPEED_X_PLAYER=7;
				SPEED_Y_MISSILE=5;
				init();
			}
			if(isAKeyDown(KeyEvent.VK_2)) {
				SPEED_X_PLAYER=9;
				SPEED_Y_MISSILE=7;
				init();
			}
			if(isAKeyDown(KeyEvent.VK_3)) {
				SPEED_X_PLAYER=11;
				SPEED_Y_MISSILE=9;
				init();
			}
			if(isAKeyDown(KeyEvent.VK_BACK_SPACE)) gameState = STATE_BEFORE;
			break;} 
		}
	
	/**
	 * Updates and draws the score in the upper right corner of the board
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawScore(Graphics2D g) {
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		g.setFont(new Font(FONT_TYPE,0,40));
		String toPrint="Score: "+myPlayer.score;
		int textWidth = g.getFontMetrics().stringWidth(toPrint);
		int textHeight = g.getFontMetrics().getHeight();
		g.drawString(toPrint, (getWidth()-textWidth), 1*textHeight);
	}
	
	/**
	 * Draws the missiles and the players
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawSprites(Graphics2D g) {
		myPlayer.draw(g);
		for (int i=0;i<myMissiles.length;i++) {
			if (myMissiles[i].getMissileDeployed()==true) myMissiles[i].draw(g);
		}

	}
	
	/**
	 * Creates the start screen
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawStartScreen(Graphics2D g) {
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		String title = "Welcome to Dodge";
		String start = "Press \"SPACE\" to start game";;
		String fullScreen= "(Use Full Screen)";
		String toInstruction = "Press \"I\" to see instructions";
		String quit = "Press \"Q\" to quit game";
		String levelChange = "Press \"D\" to change difficulty";
		g.setFont(new Font(FONT_TYPE,0,60));
		int textHeight = g.getFontMetrics().getHeight();
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth()-titleWidth)/2,textHeight*2);
		g.setFont(new Font(FONT_TYPE,0,30));
		int fullScreenWidth = g.getFontMetrics().stringWidth(fullScreen);
		g.drawString(fullScreen, (getWidth()-fullScreenWidth)/2, 3*textHeight);	
		g.setFont(new Font(FONT_TYPE,0,40));
		int startWidth = g.getFontMetrics().stringWidth(start);
		int quitWidth = g.getFontMetrics().stringWidth(quit);
		int levelChangeWidth = g.getFontMetrics().stringWidth(levelChange);
		g.drawString(start, (getWidth()-startWidth)/2, getHeight()*4/5);	
		g.drawString(quit, (getWidth())-quitWidth-30, getHeight()-textHeight);	
		g.drawString(toInstruction, 30, getHeight()-textHeight);	
		g.drawString(levelChange, (getWidth()-levelChangeWidth)/2, getHeight()-textHeight);	
	}
	
	/**
	 * Creates the instruction screen
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawInstructionScreen(Graphics2D g) {
		drawBoard(g);
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		String title = "Instructions";
		String objective1 = "Answer the multiplication question! Question changes every 5 seconds.";
		String objective2 = "Avoid the wrong-colored lasers! Touch the right-colored lasers!";
		String move= "Use arrow keys (left or right) to move your ship.";
		String goBack = "(Press \"BACKSPACE\" to go back to start screen)";
		g.setFont(new Font(FONT_TYPE,0,60));
		int textHeight = g.getFontMetrics().getHeight();
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth()-titleWidth)/2,textHeight*2);
		g.setFont(new Font(FONT_TYPE,0,40));
		int moveWidth = g.getFontMetrics().stringWidth(move);
		int objective1Width =  g.getFontMetrics().stringWidth(objective1);
		int objective2Width =  g.getFontMetrics().stringWidth(objective2);
		int goBackWidth =  g.getFontMetrics().stringWidth(goBack);
		g.drawString(objective1, (getWidth()-objective1Width)/2,getHeight()/2);
		g.drawString(objective2, (getWidth()-objective2Width)/2,getHeight()*9/16);
		g.drawString(move, (getWidth()-moveWidth)/2, getHeight()*11/16);		
		g.drawString(goBack, (getWidth()-goBackWidth)/20, getHeight()-5-textHeight);	
	}
	
	/**
	 * Creates the level screen
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawLevelScreen(Graphics2D g) {
		drawBoard(g);
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		String title = "Game Difficulty";
		String easy= "Press \"1\" - Easy (default)";
		String moderate= "Press \"2\" - Moderate";
		String hard= "Press \"3\" - Hard";
		String goBack = "(Press \"BACKSPACE\" to go back to start screen)";
		g.setFont(new Font(FONT_TYPE,0,60));
		int textHeight = g.getFontMetrics().getHeight();
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth()-titleWidth)/2,textHeight*2);
		g.setFont(new Font(FONT_TYPE,0,40));
		int goBackWidth =  g.getFontMetrics().stringWidth(goBack);
		int easyWidth = g.getFontMetrics().stringWidth(easy);
		int moderateWidth = g.getFontMetrics().stringWidth(moderate);
		int hardWidth = g.getFontMetrics().stringWidth(hard);
		g.drawString(easy, (getWidth()-easyWidth)/2,getHeight()/2);
		g.drawString(moderate, (getWidth()-moderateWidth)/2,getHeight()*9/16);	
		g.drawString(hard, (getWidth()-hardWidth)/2,getHeight()*5/8);	
		g.drawString(goBack, (getWidth()-goBackWidth)/20, getHeight()-5-textHeight);	
	}
	
	/**
	 * Creates the end screen after each round
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawEndScreen(Graphics2D g) {
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		String title = "Game Over";
		String scoreSheet = "Your Score: "+myPlayer.score;
		String restart=" Press \"Y\" or \"N \" to continue";
		g.setFont(new Font(FONT_TYPE,0,60));
		int textHeight = g.getFontMetrics().getHeight();
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (getWidth()-titleWidth)/2,textHeight*2);
		g.setFont(new Font(FONT_TYPE,0,40));
		int restartWidth = g.getFontMetrics().stringWidth(restart);
		int scoreSheetWidth =  g.getFontMetrics().stringWidth(scoreSheet);
		g.drawString(scoreSheet, (getWidth()-scoreSheetWidth)/2,getHeight()*9/16);
		g.drawString(restart, (getWidth()-restartWidth)/2, getHeight()-textHeight);
		
	}

	/**
	 * Draws the board
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawBoard(Graphics2D g) {
		g.drawImage(BG_SPACE, 0, 0,getWidth(),getHeight(), null);
	}
	
	/**
	 * Draws up the end game screen after quitting 
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawQuitScreen(Graphics2D g){
		g.drawImage(BG_QUIT, 0, 0,getWidth(),getHeight(), null);
	}
	
	/**
	 * Sets a new multiplication question and answer choices
	**/
	private void changeQuestion() {
		Random rand = new Random();
		int  n1 = rand.nextInt(10)+1;
		int  n2 = rand.nextInt(10)+1;
		
		currentQuestion="What is "+n1+" times "+n2+"?";
		
		currentAnswer = n1*n2;
		
		int possibleAnswer1 = Math.abs((n1-3)*(n2+7));
		int possibleAnswer2 = Math.abs((n1-8)*(n2+2));
		
		correctChoice=rand.nextInt(3)+1;
		
		if (correctChoice==1) currentChoices="Red) "+currentAnswer+"          Green) "+possibleAnswer1+"          Yellow) "+possibleAnswer2;
		else if (correctChoice==2) currentChoices="Red) "+possibleAnswer1+"          Green) "+currentAnswer+"          Yellow) "+possibleAnswer2;
		else currentChoices="Red) "+possibleAnswer2+"          Green) "+possibleAnswer1+"          Yellow) "+currentAnswer;
	}
	
	/**
	 * Displays the current question and its answer choices 
	 * 
	 * @param g a graphics object of the second dimension
	**/
	private void drawQuestion(Graphics2D g) {
		g.setColor(Color.getHSBColor((float)0, (float)0, (float)1));
		g.setFont(new Font(FONT_TYPE,0,40));
		String toPrint=currentQuestion;
		int textWidth = g.getFontMetrics().stringWidth(toPrint);
		int textHeight = g.getFontMetrics().getHeight();
		g.drawString(toPrint, (getWidth()-textWidth)/2, 1*textHeight);
		
		toPrint=currentChoices;
		textWidth = g.getFontMetrics().stringWidth(toPrint);
		textHeight = g.getFontMetrics().getHeight();
		g.drawString(toPrint, (getWidth()-textWidth)/2, 3*textHeight);
	}
	
	/**
	 * Tests if player can move
	 * 
	 * @return true if the player can move, or not on the sides
	 * @param myPlayer the player object
	 * @param moveLeftOrRight checks if the player can move
	**/
	public boolean canMove(Player myPlayer, boolean moveLeftOrRight){ 
		if(moveLeftOrRight){
			if(myPlayer.getPosX()<=myPlayer.getSpeedX()) return false; 
			else return true;
		}
		if(myPlayer.getPosX()+myPlayer.getSizeX()>=getWidth()-myPlayer.getSpeedX())return false;
		else return true;
	}
	
	/**
	 * Main method
	 * Instantiates DodgeGame, creates game frame, starts game, opens and closes start-up animation
	 * 
	 * @throws MalformedURLException
	 */
	public static void main(String[] args)  throws MalformedURLException {
		MultiplicationGame myGame=new MultiplicationGame();
		createGameFrame(myGame, 2000, 2000); //fills entire screen
		
		myGame.init();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
