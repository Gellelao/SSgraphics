package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import src.model.Board;
import src.model.Player;
import src.model.Token;

/**
 * Extension of the AbstractGamePanel for drawing a player's available tokens in a grid.
 * This program uses two of these, one on either side of the boardPanel
 * 
 * @author Deacon
 *
 */
public class PlayerPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private Player player;
	private Color colour;
	private Board myModel;
	private PlayerPanelController control;

	private ArrayList<TokenRegion> regions;

	public PlayerPanel(Board model, String player) {
		myModel = model;
		control = new PlayerPanelController(this);

		// Add a PlayerPanelController as the key and mouse listeners
		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(false);

		regions = new ArrayList<TokenRegion>();

		// Change background depending on player
		if(player.equals("p1")) {
			this.player = model.getP1();
			this.colour = new Color(255, 249, 81);
		}
		else {
			this.player = model.getP2();
			this.colour = new Color(86, 196, 64);
		}
	}

	@Override
	protected void drawAll(Graphics2D g) {
		regions.clear();
		
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Unlike other panels, this one draws the given player's available tokens,
		// which are accessed through the model (board)
		super.drawGrid(g, myModel.getAvailable(player));
	}

	@Override
	public void addRegion(TokenRegion r) {
		if(r.getToken() == null)return;
		regions.add(r);
	}

	@Override
	protected Controller getController() {
		return control;
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

	public Player getPlayer(){
		return player;
	}
}
