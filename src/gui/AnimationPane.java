package src.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import src.model.Token;

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
	
	public void drawAll(Graphics2D g){
		if(currentToken == null)return;
		if(currentlyDrawing){
			super.drawToken(g, currentX, currentY, currentSize, currentToken);
		}
	}
	
	public void updateDrawing(){
		if(currentX < targetX){
			currentX += (targetX - currentX) / 2;
		}
		else if(currentX > targetX){
			currentX -= (currentX - targetX) / 2;
		}
		if(currentY < targetY){
			currentY += (targetY - currentY) / 2;
		}
		else if(currentY > targetY){
			currentY -= (currentY - targetY) / 2;
		}
		int differenceX = Math.abs(targetX - currentX);
		int differenceY = Math.abs(targetY - currentY);
		if(differenceX < 2 && differenceY < 2)currentlyDrawing = false;
	}
	
	public void startDrawing(){
		currentlyDrawing = true;
	}
	
	public boolean currentlyDrawing(){
		//System.out.println("Returning " + currentlyDrawing);
		return currentlyDrawing;
	}
	
	public void setCurrent(int x, int y, int size, Token token){
		this.currentX = x;
		this.currentY = y;
		this.currentSize = size;
		this.currentToken = token;
	}
	
	public void setCurrent(TokenRegion r){
		this.currentX = r.getX();
		this.currentY = r.getY();
		this.currentSize = r.getSize();
		this.currentToken = r.getToken();
	}
	
	public void setTarget(int x, int y){
		this.targetX = x;
		this.targetY = y;
	}

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
