package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import src.model.Board;
import src.model.PlayerToken;
import src.model.Token;

public class PlayerPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Color colour;
	private Board myModel;
	private PlayerPanelController control;

	private ArrayList<TokenRegion> regions;

	public PlayerPanel(Board model, String player) {
		myModel = model;
		control = new PlayerPanelController(myModel, this);

		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(false);

		regions = new ArrayList<TokenRegion>();

		if(player.equals("p1")) {
			this.player = model.getP1();
			this.colour = new Color(255, 249, 81);
		}
		else {
			this.player = model.getP2();
			this.colour = new Color(86, 196, 64);
		}
	}

	public Token getToken(int x, int y){
		for(TokenRegion r : regions){
			if(r.contains(x, y))return r.getToken();
		}
		return null;
	}

	@Override
	protected Color getBGColour() {
		return colour;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		regions.clear();
		super.drawGrid(g, myModel.getAvailable(player));
	}

	@Override
	protected Controller getController() {
		return control;
	}

	@Override
	public void addRegion(TokenRegion r) {
		if(r.getToken() == null)return;
		regions.add(r);
	}

	public PlayerToken getPlayer(){
		return player;
	}
}
