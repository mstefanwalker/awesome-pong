package pongGame;

import java.util.Random;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Main {
	
	static Random randomGen = new Random();
	
	private static final int FRAMERATE = 10;
	static final int STEP_SPEED = 10;
	private static final String GAME_TITLE = "Bit World";
	private static final int xDimension = 60;
	private static final int yDimension = 40;
	
	static boolean step1Run = true;
	static boolean end = false;
//	static int[][] world = new int[xDimension][yDimension];
	static float[] xScreenGrid = new float[xDimension + 1];
	static float[] yScreenGrid = new float[xDimension + 1];
	
	static boolean secondaryThreadsTerminated = false;
	
	static boolean fullscreen;
	
	int cellWidth;
	int cellHight;
	
	public static float randomRed;
	public static float randomGreen;
	public static float randomBlue;
	
	Step step1;
	Counter counter1;
	Input input1;
	
	//the following variables just have to do with manipulating the "fuzzy screen"
	float cycleColorR = .7f;
	float cycleColorG = .7f;
	float cycleColorB = .7f;
	float addColorR = .9f;
	float addColorG = .9f;
	float addColorB = .9f;
	float rangeColorR = .01f;
	float rangeColorG = .01f;
	float rangeColorB = .01f;
	float startColorR = .1f;
	float startColorG = .1f;
	float startColorB = .1f;
	
//	public static void main(String[] args){
//		new NotTheRealMain();
//	}
	
	public Main(){		

		try{
			initiate();
			run();
		} catch(Exception e) {
			e.printStackTrace(System.err);
			Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
		}
		finally{
			cleanup();
	    }
	    System.exit(0);
	}
	
	public void run() {
		while(!end) {
			Display.update();
			
			if(Display.isCloseRequested()) {
				end = true;
			} else if(Display.isActive()){
				renderBackground();
				Display.sync(FRAMERATE);
			} else {
				try{
					Thread.sleep(100);
				} catch(InterruptedException e) {}
				
				if(Display.isVisible() || Display.isDirty()) {
					renderBackground();
				}
			}
		}
	}
	
	public void initiate() throws Exception{
		Display.setTitle(GAME_TITLE);
		Display.setFullscreen(true);
		Display.setVSyncEnabled(true);
		Display.create();
		
		cellWidth = Display.getDisplayMode().getWidth() / xDimension;
		cellHight = Display.getDisplayMode().getHeight() / yDimension;
		
		for(int i = 0; i < xDimension; i++){
			xScreenGrid[i] = cellWidth * i;
		}
		for(int j = 0; j < yDimension; j++){
			yScreenGrid[j] = cellHight * j;
		}
		counter1 = new Counter("Counter 1");
		counter1.start();

		step1 = new Step ("Step 1");
		step1.start();
		
		input1 = new Input("Input 1");
		input1.start();
	}
	
	public void cleanup() {
		secondaryThreadsTerminated = true;
		Display.destroy();
	}
	
	public void renderBackground() {
		Draw.initiate();
		for(int i = 0; i < xDimension; i++){
			for(int j = 0; j < yDimension; j++){
				Draw.origin((int)(xScreenGrid[i]), (int)(yScreenGrid[j]));
				Draw.quadrilateral(0, 0, 0, cellHight, cellWidth, cellHight, cellWidth, 0,
						startColorR + rangeColorR*(cycleColorR*randomRed + addColorR*Main.randomGen.nextFloat()), 
						startColorG + rangeColorG*(cycleColorG*randomBlue + addColorG*Main.randomGen.nextFloat()), 
						startColorB + rangeColorB*(cycleColorB*randomGreen + addColorB*Main.randomGen.nextFloat()));
			}
		}
	}
}

class Step extends Thread{
	
	public Step(String threadName) {
		super(threadName);
	}
	
	public void run() {
		while(!Main.secondaryThreadsTerminated) {
			if(Main.step1Run) {
				Main.step1Run = false;
				logic();
			}
		}
	}
	
	public void logic() {
		Main.randomRed = Main.randomGen.nextFloat();
		Main.randomBlue = Main.randomGen.nextFloat();
		Main.randomGreen = Main.randomGen.nextFloat();
	}
}

class Counter extends Thread{
	public Counter(String threadName) {
		super(threadName);
	}
	
	public void run() {
		while(!Main.secondaryThreadsTerminated){
			try {
				Thread.sleep(Main.STEP_SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.step1Run = true;
		}
	}
}

class Input extends Thread{
	public Input(String threadName) {
		super(threadName);
	}
	
	public void run() {
		while(!Main.secondaryThreadsTerminated){
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Main.end = true;
			}
		}
	}
}
