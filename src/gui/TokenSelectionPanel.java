package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import src.model.Board;
import src.model.Token;

public class TokenSelectionPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;

	private Color colour;
	private TokenSelectionPanelController control;
	private Board myModel;
	
	private Token toDraw;
	
	private ArrayList<TokenRegion> regions;

	public TokenSelectionPanel(Board model, String player) {
		myModel = model;
		control = new TokenSelectionPanelController(myModel, this);
		
		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(false);
		
		regions = new ArrayList<TokenRegion>();
		
		if(player.equals("p1")) {
			this.colour = new Color(255, 249, 81);
		}
		else {
			this.colour = new Color(86, 196, 64);
		}
	}
	
	public void setTokenToDraw(Token t){
		toDraw = t;
	}
	
	public TokenRegion getRegion(int x, int y){
		for(TokenRegion r : regions){
			if(r.contains(x, y)){
				return r;
			}
		}
		return null;
	}
	
	@Override
	protected void drawAll(Graphics2D g) {
		regions.clear();

		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		super.drawGrid(g, myModel.getRotations(toDraw));
	}

	@Override
	protected Color getBGColour() {
		return colour;
	}

	@Override
	protected Controller getController() {
		return control;
	}

	@Override
	public void addRegion(TokenRegion r) {
		regions.add(r);
	}

	
}
