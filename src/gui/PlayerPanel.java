package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.PlayerToken;

public class PlayerPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Color colour;
	private Board myModel;
	private PlayerPanelController control;

	public PlayerPanel(Board model, String player) {
		control = new PlayerPanelController(myModel, this);
		myModel = model;
		
		this.addKeyListener(control);
		this.addMouseListener(control);
		
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
	protected Color getBGColour() {
		return colour;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		super.drawGrid(g, myModel.getAvailable(player));
	}

	@Override
	protected Controller getController() {
		return control;
	}
}
