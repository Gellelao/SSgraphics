package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import src.model.Token;

public class TokenRegion {
	private Token token;
	private int x;
	private int y;
	private int size;
	
	public TokenRegion(Token t, int x, int y, int size) {
		this.token = t;
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public boolean contains(int pX, int pY) {
		if(pX > x && pX < x+size &&
		   pY > y && pY < y+size)return true;
		return false;
	}
	
	public String checkSubregions(int clickX, int clickY){
		int thickness = size/5;
		if(clickX > x && clickX < x+size && clickY > y && clickY < y+thickness){return "up";} // Clicked North
		if(clickX > x+size-thickness && clickX < x+size && clickY > y && clickY < y+size){return "right";} // Clicked East
		if(clickX > x && clickX < x+size && clickY > y+size-thickness && clickY < y+size){return "down";} // Clicked South
		if(clickX > x && clickX < x+thickness && clickY > y && clickY < y+size){return "left";} // Clicked West
		return null;
	}
	
	public Token getToken(){
		return token;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	
	public void show(Graphics2D g){
		g.setColor(Color.WHITE);
		g.drawRect(x, y, size, size);
	}
}
