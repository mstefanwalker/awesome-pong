package pongGame;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Draw {
	// draws everything!
	 
	public static void initiate() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
	}
	
	public static void origin(int x, int y) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
	}

	public static void quadrilateral(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float r, float g, float b) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(r, g, b);
		GL11.glVertex2f(x1,y1);
		GL11.glVertex2f(x2,y2);
		GL11.glVertex2f(x3,y3);
		GL11.glVertex2f(x4,y4);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void quadrilateral(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float scale, float r, float g, float b) {
		// for scale, 1 = unscaled 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(r, g, b);
		GL11.glVertex2f(x1*scale,y1*scale);
		GL11.glVertex2f(x2*scale,y2*scale);
		GL11.glVertex2f(x3*scale,y3*scale);
		GL11.glVertex2f(x4*scale,y4*scale);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void text1(float scale, float r, float g, float b) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(r, g, b);
		GL11.glVertex2f(1*scale,1*scale);
		GL11.glVertex2f(1*scale,1*scale);
		GL11.glVertex2f(1*scale,1*scale);
		GL11.glVertex2f(1*scale,1*scale);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void text2() {
		
	}
}
