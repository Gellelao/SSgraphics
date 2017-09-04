package src.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import src.model.Token;

/**
 * This class is responsible for animating tokens. It is used to draw the animation when creating
 * and moving tokens. It takes information about the location of its start position and target, and 
 * draws between those locations by updating the 'current' fields
 * 
 * @author Deacon
 *
 */
public class AnimationPane extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	
	private Token currentToken;
	private int currentX;
	private int currentY;
	private int currentSize;
	
	private int targetX;
	private int targetY;
	
	final static int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	final static int screenHeight= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	
	boolean currentlyDrawing;

	public AnimationPane() {
		this.setFocusable(false);
		currentlyDrawing = false;
	}
	
	public void paintComponent(Graphics _g) {
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		drawAll(g);
	}
	
	/**
	 * Draws the currently animating token (if there is one)
	 */
	public void drawAll(Graphics2D g){
		if(currentToken == null)return;
		if(currentlyDrawing){
			super.drawToken(g, currentX, currentY, currentSize, currentToken);
		}
	}
	
	/**
	 * Updates the position of the moving token
	 * The amount moved is based on a division of the remaining distance, so
	 * the token should technically never reach it's target, thats why the check on
	 * the last line has such a large range (difference < 9).
	 * This way of doing it results in the appearance of deceleration near the end of the token's path
	 */
	public void updateDrawing(){
		if(currentX < targetX){
			currentX += (targetX - currentX) / 8;
		}
		else if(currentX > targetX){
			currentX -= (currentX - targetX) / 8;
		}
		if(currentY < targetY){
			currentY += (targetY - currentY) / 8;
		}
		else if(currentY > targetY){
			currentY -= (currentY - targetY) / 8;
		}
		int differenceX = Math.abs(targetX - currentX);
		int differenceY = Math.abs(targetY - currentY);
		
		// If the token is 'close enough,' stop drawing.
		if(differenceX < 9 && differenceY < 9)currentlyDrawing = false;
	}
	
	public void startDrawing(){
		currentlyDrawing = true;
	}
	
	public boolean currentlyDrawing(){
		return currentlyDrawing;
	}
	
	/**
	 * Sets the start position of the animation, along with the token to draw, and its size
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param token
	 */
	public void setCurrent(int x, int y, int size, Token token){
		this.currentX = x;
		this.currentY = y;
		this.currentSize = size;
		this.currentToken = token;
	}
	
	/*
	 * Takes a TokenRegion, r, and uses that to find the x, y, size, and token of the start of the animation
	 */
	public void setCurrent(TokenRegion r){
		this.currentX = r.getX();
		this.currentY = r.getY();
		this.currentSize = r.getSize();
		this.currentToken = r.getToken();
	}
	
	/**
	 * Sets the target of the animation to be based on the current position and a direction. Due to the way this is called
	 * from a SuperController, the current position will always be set before this method is called.
	 * 
	 * @param direction
	 */
	public void setMove(String direction){
		switch(direction){
		case "up":
			setTarget(currentX, currentY-currentSize);
			return;
		case "down":
			setTarget(currentX, currentY+currentSize);
			return;
		case "left":
			setTarget(currentX-currentSize, currentY);
			return;
		case "right":
			setTarget(currentX+currentSize, currentY);
			return;
		}
	}
	
	public void setTarget(int x, int y){
		this.targetX = x;
		this.targetY = y;
	}

	// These methods are not used by this panel, because this only needs to draw tokens
	@Override
	protected Color getBGColour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Controller getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRegion(TokenRegion r) {
		// TODO Auto-generated method stub
		
	}
	
	public Dimension getPreferredSize() {return new Dimension(screenWidth, screenHeight);} // TODO: Change these back to variables
}
