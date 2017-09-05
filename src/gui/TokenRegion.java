package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import src.model.Token;

/**
 * An object of this class stores information about a region of the screen that corresponds to a Token.
 * Using the fields, the region is the square with top right corner at x, y, with height and width of 'size'
 * If the user clicks any point in this region, the 'token' field can be returned as the Token the user has clicked.
 * 
 * @author Deacon
 *
 */
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
	
	/**
	 * @param pX - the x of the point to check
	 * @param pY - the y of the point to check
	 * @return whether or not the point [pX, pY] is within this TokenRegion
	 */
	public boolean contains(int pX, int pY) {
		return(pX > x && pX < x+size &&
		       pY > y && pY < y+size);
	}
	
	/**
	 * Checks if the user has clicked on any edges of this TokenRegion. Edges span the whole length of each side,
	 * and have a 'thickness' calculated using the tokenRegions's size.
	 * Because they overlap at the corners, clicking on a corner returns the direction that is checked first below
	 * 
	 * @param pX - the x of the point to check
	 * @param pY - the y of the point to check
	 * @return a string identifying which direction has been clicked, or null if the point [pX, pY] is not on any edges
	 */
	public String checkSubregions(int pX, int pY){
		int thickness = size/5;
		if(pX > x                && pX < x+size      && pY > y                && pY < y+thickness){return "up"   ;} // Clicked North
		if(pX > x                && pX < x+size      && pY > y+size-thickness && pY < y+size)     {return "down" ;} // Clicked South
		if(pX > x                && pX < x+thickness && pY > y                && pY < y+size)     {return "left" ;} // Clicked West
		if(pX > x+size-thickness && pX < x+size      && pY > y                && pY < y+size)     {return "right";} // Clicked East
		return null;
	}
	
	/**
	 * Draws a white border around this tokenRegion
	 * 
	 * @param g
	 */
	public void show(Graphics2D g){
		g.setColor(Color.WHITE);
		g.drawRect(x, y, size, size);
	}
	
	public Token getToken(){return token;}
	public int getX()      {return x;}
	public int getY()      {return y;}
	public int getSize()   {return size;}
}
