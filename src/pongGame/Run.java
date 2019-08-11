package pongGame;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class Run {
	
	// game title
	public static final String GAME_TITLE = "Pong";
	
	private static final int FRAMERATE = 60;
	//for background
	static final int STEP_SPEED = 10;
	private static final int xDimension = 60;
	private static final int yDimension = 40;
	
	static float[] xScreenGrid = new float[xDimension + 1];
	static float[] yScreenGrid = new float[xDimension + 1];
	
	static boolean secondaryThreadsTerminated = false;
	
	static boolean fullscreen;
	static boolean step1Run = true;
	
	static int cellWidth;
	static int cellHight;
	
	public static float randomR;
	public static float randomG;
	public static float randomB;
	
	//the following variables just have to do with manipulating the "fuzzy screen"
	static final float CYCLE_COLOR_R = .15f; //.2
	static final float CYCLE_COLOR_G = .2f; //.3
	static final float CYCLE_COLOR_B = .15f; //.2
	static final float ADD_COLOR_R = .06f; //.1
	static final float ADD_COLOR_G = .06f; //.1
	static final float ADD_COLOR_B = .06f; //.1
	static final float RANGE_COLOR_R = .03f; //.03
	static final float RANGE_COLOR_G = .03f; //.03
	static final float RANGE_COLOR_B = .03f; //.03S
	static final float START_COLOR_R = .05f; //.15
	static final float START_COLOR_G = .05f; //.15
	static final float START_COLOR_B = .05f; //.15
	static float cycleColorR = CYCLE_COLOR_R;
	static float cycleColorG = CYCLE_COLOR_G;
	static float cycleColorB = CYCLE_COLOR_B;
	static float addColorR = ADD_COLOR_R;
	static float addColorG = ADD_COLOR_G;
	static float addColorB = ADD_COLOR_B;
	static float rangeColorR = RANGE_COLOR_R;
	static float rangeColorG = RANGE_COLOR_G;
	static float rangeColorB = RANGE_COLOR_B;
	static float startColorR = START_COLOR_R;
	static float startColorG = START_COLOR_G;
	static float startColorB = START_COLOR_B;
	//end of background -----------------------------------
	
	private static boolean finished;
	
	//counts down, when positive, ball yellow.
	private static int powerUsed = 0;
	private static final int FLASH_FREQUENCY = 10;
	private static int lPower = 0;
	private static int rPower = 0;
	private static int powerThreshold = 5000; //3, make this number big to remove powers
	private static final int POWER_DELAY = 20;
	private static int powerDelay = 0;
	private static final int FLASH_DELAY = 5;
	private static int flashDelay = 0;

	private static boolean blnPowered = false;
	
	private static final int RESET_COUNTER = 75;
	private static int resetCounter = RESET_COUNTER;
	
	// left and right paddle y positions
	private static int ry = Display.getDisplayMode().getHeight() / 2;
	private static int ly = Display.getDisplayMode().getHeight() / 2;
	
	// coordinate point of the ball
	private static int bx = Display.getDisplayMode().getWidth() / 2;
	private static int by = Display.getDisplayMode().getHeight() / 2;
	
	// direction of the ball (1-4)
	private static int bDirect = 1;
	// next post-wall-collision direction
	private static int bDirectNextWall = 0;
	// next post-paddle-collision direction
	private static int bDirectNextPaddle = 0;

	// ball and paddle speed
	private final static int P_SPEED = 15; //9, 15, 15
	private final static int B_SPEED = 14; //7, 12, 14
	private static int pSpeed = P_SPEED; //9
	private static int bSpeed = B_SPEED; //8
	
	//last paddle direction, (1 is up, 0 down)
	private static int rPDirect = 1;
	private static int lPDirect = 1;
	
	// width of half of the ball, length from the side to the center 
	private static int bLength = 20; //20
	//paddle length and width, same preportion as the ball
	private static int pLength = 80; //80
	private static int pWidth = 20; //20
	
	private static int rally = 0;
	
	static Random randomGenerator = new Random();
	//background
	static Random randomGen = new Random();
	 
	// score
	private static int rScore = 0;
	private static int lScore = 0;
	
	public static void main(String[] args){
		boolean fullscreen = true/*(args.length == 1 && args[0].equals("-fullscreen"))*/;
		 
		try{
	      init(fullscreen);
	      initiate();
	      run();
	      System.out.println(rally);
	      System.out.println(lScore + " " + rScore);
	    } 
		catch(Exception e){
			e.printStackTrace(System.err);
			Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
	    } 
		finally{
			cleanup();
	    }
	    System.exit(0);
	}

	private static void init(boolean fullscreen) throws Exception{
	    // Create a fullscreen window with 1:1 orthographic 2D projection (default)
	    Display.setTitle(GAME_TITLE);
	    Display.setFullscreen(fullscreen);
	    
	    Display.setVSyncEnabled(true);
	    
	    Display.create();
	}
	
	private static void cleanup(){
		Display.destroy();
	}
	
	private static void run(){
		while(!finished){
			
			Display.update();
			
			if(Display.isCloseRequested()){
				finished = true;
			}
			
			else if(Display.isActive()){
				logic();
//				randomBallDirection();//B)
				renderBackground();
				render();
			}
			else{
				try{
					Thread.sleep(100);
				}
				catch(InterruptedException e){}
				
				logic();
				
				if(Display.isVisible() || Display.isDirty()){
					render();
				}
			}
		}
	}
	
	public static void initiate(){
		
		cellWidth = Display.getDisplayMode().getWidth() / xDimension;
		cellHight = Display.getDisplayMode().getHeight() / yDimension;
		
		for(int i = 0; i < xDimension; i++){
			xScreenGrid[i] = cellWidth * i;
		}
		for(int j = 0; j < yDimension; j++){
			yScreenGrid[j] = cellHight * j;
		}
	}	
	
	private static void reset(){
//		ry = Display.getDisplayMode().getHeight() / 2;
//		ly = Display.getDisplayMode().getHeight() / 2;
		
		bx = Display.getDisplayMode().getWidth() / 2;
		by = Display.getDisplayMode().getHeight() / 2;
		
		bDirectNextWall = 0;
		bDirectNextPaddle = 0;
		
		randomBallDirection();
		
		System.out.println(rally);
		rally = 0;
		updateGameVariables(rally);
		resetCounter = RESET_COUNTER;
	}
	private static void randomBallDirection() {
		int sswitch = randomGenerator.nextInt(4);
		switch(sswitch){
		case 0:
			bDirect = 1;
			break;
		case 1:
			bDirect = 2;
			break;
		case 2:
			bDirect = 3;
			break;
		case 3:
			bDirect = 4;
			break;
		}
	}
	
	private static void logic(){
		
		randomR = randomGen.nextFloat();
		randomB = randomGen.nextFloat();
		randomG = randomGen.nextFloat();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			finished = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			reset();
		}
		
		// left paddle controls and collision
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
			rPDirect = 1;
			if(ly + pSpeed + pLength >= Display.getDisplayMode().getHeight()){
				ly = Display.getDisplayMode().getHeight() - pLength;
			}
			else{
				ly = ly + pSpeed;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_L)) {
			rPDirect = 0;
			if(ly - pSpeed - pLength <= 0){
				ly = pLength;
			}
			else{
				ly = ly - pSpeed;
			}
		}
		
		// right paddle controls and collision
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			lPDirect = 1;
			if(ry + pSpeed + pLength >= Display.getDisplayMode().getHeight()){
				ry = Display.getDisplayMode().getHeight() - pLength;
			}
			else{
				ry = ry + pSpeed;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			lPDirect = 0;
			if(ry - pSpeed - pLength <= 0){
				ry = pLength;
			}
			else{
				ry = ry - pSpeed;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && lPower >= powerThreshold) {
			lPower = 0;
			blnPowered = true;
			powerDelay = POWER_DELAY;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_I) && rPower >= powerThreshold) {
			rPower = 0;
			blnPowered = true;
			powerDelay = POWER_DELAY;
		}
		//UPDATE COUNTERS--------------------------mmmmmmmmmmm
		resetCounter--;
		powerUsed--;
		powerDelay--;
		flashDelay--;
		if(powerDelay > 0){
			randomBallDirection();
			powerUsed = 20;
		}
		
		// ball direction and collision w/ top and bottom
		// first if() is top collision, second if() is bottom collision
		if(by + bSpeed + bLength >= Display.getDisplayMode().getHeight()){
			if(bDirectNextWall != 0){
				by = Display.getDisplayMode().getHeight() - bLength;
				switch(bDirectNextWall){
					case 3:
						bDirect = bDirectNextWall;
						bDirectNextWall = 0;
						break;
					case 4:
						bDirect = bDirectNextWall;
						bDirectNextWall = 0;
						break;
				}
			}
			switch(bDirect){
				case 1:
					bDirectNextWall = 4;
					break;
				case 2:
					bDirectNextWall = 3; // 3 B) ------------------------------------------------
					break;
			}
		}
		if(by - bSpeed - bLength <= 0){
			if(bDirectNextWall != 0){
				by = bLength;
				switch(bDirectNextWall){
					case 1:
						bDirect = bDirectNextWall;
						bDirectNextWall = 0;
						break;
					case 2:
						bDirect = bDirectNextWall;
						bDirectNextWall = 0;
						break;
				}
			}
			switch(bDirect){
				case 3:
					bDirectNextWall = 2;
					break;
				case 4:
					bDirectNextWall = 1;
					break;
			}
		}
		// ball colision with paddles
		// first if() is right paddle collision
		// second if() is left paddle collision
		if(bx + bSpeed >= Display.getDisplayMode().getWidth() - 90){
			if(by + bLength + pLength >= ly && by - bLength - pLength <= ly && bx <= Display.getDisplayMode().getWidth() - 90 + bSpeed){ 
				if(bDirectNextPaddle != 0){
					bx = Display.getDisplayMode().getWidth() - 90;
					switch(bDirectNextPaddle){
						case 2:
							bDirect = bDirectNextPaddle;
							bDirectNextPaddle = 0;
							break;
						case 3:
							bDirect = bDirectNextPaddle;
							bDirectNextPaddle = 0;
							break;
					}
					rally++;
					rPower++;
					updateGameVariables( rally );
				}
				
				switch(bDirect){
					case 1:
						bDirectNextPaddle = 2;
						break;
					case 4:
						bDirectNextPaddle = 3;
						break;
				}
			}
		}
		// (second if())
		if(bx - bSpeed <= 90){
			if(by + bLength + pLength >= ry && by - bLength - pLength <= ry && bx >= 90 - bSpeed){
				if(bDirectNextPaddle != 0){
					bx = 90;
					switch(bDirectNextPaddle){
						case 1:
							bDirect = bDirectNextPaddle;
							bDirectNextPaddle = 0;
							break;
						case 4:
							bDirect = bDirectNextPaddle;
							bDirectNextPaddle = 0;
							break;
					}
					rally++;
					lPower++;
					updateGameVariables( rally );
				}
				switch(bDirect){
					case 2:
						bDirectNextPaddle = 1;
						break;
					case 3:
						bDirectNextPaddle = 4;
						break;
				}

			}
		}
		// reset when out of bounds
		if(bx <= bLength){
			reset();
			rScore++;
		}
		if(bx >= Display.getDisplayMode().getWidth() - bLength){
			reset();
			lScore++;
		}
		// based on the direction variable, move according to the speed
		if( resetCounter < 0){
			switch(bDirect){
				case 1:
					bx = bx + bSpeed;
					by = by + bSpeed;
					break;
				case 2:
					bx = bx - bSpeed;
					by = by + bSpeed;
					break;
				case 3:
					bx = bx - bSpeed;
					by = by - bSpeed;
					break;
				case 4:
					bx = bx + bSpeed;
					by = by - bSpeed;
					break;	
			}
		}
		
	}// end logic
	
	static boolean flash = true;
	public static void updateGameVariables( int rally ){
		bSpeed = B_SPEED + (int)(.15*rally);
		pSpeed = P_SPEED + (int)(.15*rally);
		cycleColorR = (float) (CYCLE_COLOR_R + (.04 * rally));
		addColorR = (float) (ADD_COLOR_R + (.02 * rally));
		if( rally % FLASH_FREQUENCY == 0 && rally > 0){
			flashDelay = FLASH_DELAY;
		}
	}
	
	//from background
	public static void renderBackground(){
		if(flashDelay > 0){
			startColorR = 0.5f;
			startColorG = 0.5f;
			startColorB = 0.5f;
		} else {
			startColorR = START_COLOR_R;
			startColorG = START_COLOR_G;
			startColorB = START_COLOR_B;
		}
		Draw.initiate();
		for(int i = 0; i < xDimension; i++){
			for(int j = 0; j < yDimension; j++){
				Draw.origin((int)(xScreenGrid[i]), (int)(yScreenGrid[j]));
				Draw.quadrilateral(0, 0, 0, cellHight, cellWidth, cellHight, cellWidth, 0,
//						startColorR + rangeColorR*(cycleColorR*randomRed + addColorR*randomGen.nextFloat()), 
//						startColorG + rangeColorG*(cycleColorG*randomBlue + addColorG*randomGen.nextFloat()), 
//						startColorB + rangeColorB*(cycleColorB*randomGreen + addColorB*randomGen.nextFloat()));
						startColorR + rangeColorR*(cycleColorR*randomR + addColorR*randomGen.nextFloat()), 
						startColorR + rangeColorR*(cycleColorR*randomR + addColorR*randomGen.nextFloat()), 
						startColorR + rangeColorR*(cycleColorR*randomR + addColorR*randomGen.nextFloat()));

			}
		}
	}
	//end background
	private static void render(){
		float rColor;
		float lColor;
		float bColor;
		
		if(lPower > powerThreshold){
			rColor = .65f;
		} else { rColor = 1;}
		if(rPower > powerThreshold){
			lColor = .65f;
		} else { lColor = 1;}
		if(powerUsed > 0){
			bColor = .2f;
		} else { bColor = 1;}
		
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
//		GL11.glLoadIdentity();
//		GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		
		// ball
		GL11.glPushMatrix();
	    GL11.glTranslatef(bx, by, 0.0f);

	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glColor3f(1f, 1f, bColor);
	     GL11.glVertex2i(bLength, bLength);
	     GL11.glVertex2i(bLength, -bLength);
	     GL11.glVertex2i(-bLength, -bLength);
	     GL11.glVertex2i(-bLength, bLength);
	    GL11.glEnd();
	 
	    GL11.glPopMatrix();
	    
	    // right paddle
	    GL11.glPushMatrix();
	    GL11.glTranslatef((2*pWidth+10), ry, 0.0f);
	 
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glColor3f(rColor, rColor, rColor);
	     GL11.glVertex2i(pWidth, pLength);
	     GL11.glVertex2i(pWidth, -pLength);
	     GL11.glVertex2i(-pWidth, -pLength);
	     GL11.glVertex2i(-pWidth, pLength);
	    GL11.glEnd();
	 
	    GL11.glPopMatrix();
	    
	    // left paddle
	    GL11.glPushMatrix();
	    GL11.glTranslatef(Display.getDisplayMode().getWidth() - (2*pWidth+10), ly, 0.0f);
	 
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glColor3f(lColor, lColor, lColor);
	     GL11.glVertex2i(pWidth, pLength);
	     GL11.glVertex2i(pWidth, -pLength);
	     GL11.glVertex2i(-pWidth, -pLength);
	     GL11.glVertex2i(-pWidth, pLength);
	    GL11.glEnd();
	 
	    GL11.glPopMatrix();
	}

}
