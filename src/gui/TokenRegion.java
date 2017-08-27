package src.gui;

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
	
	public Token getToken(){
		return token;
	}
}
