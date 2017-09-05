package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import src.model.Board;
import src.model.Token;

/**
 * Panel used to draw four rotations of a token, and let the user select one. This extends
 * AbstractGamePanel so it inherits the drawGrid() method, which is used to draw the four tokens
 * 
 * @author Deacon
 *
 */
public class TokenSelectionPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;

	private Color colour;
	private TokenSelectionPanelController control;
	private Board myModel;
	
	private Token toDraw;
	
	private ArrayList<TokenRegion> regions;

	public TokenSelectionPanel(Board model, String player) {
		myModel = model;
		control = new TokenSelectionPanelController(this);
		
		// Add a TokenSelectionPanelController as the key and mouse listeners
		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(false);
		
		regions = new ArrayList<TokenRegion>();
		
		// background colour depends on the player
		if(player.equals("p1")) {
			this.colour = new Color(255, 249, 81);
		}
		else {
			this.colour = new Color(86, 196, 64);
		}
	}
	
	// This method is called once the user has selected the token they want,
	// so that this panel can show the user the available rotations of that token
	public void setTokenToDraw(Token t){
		toDraw = t;
	}
	
	/**
	 * Finds if there is a TokenRegion on this panel that contains the given x and y coords
	 * 
	 * @param x
	 * @param y
	 * @return - the TokenRegion that contains x and y, or null if none of the regions contain it
	 */
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

		// Draw the background
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Here, this panel draws rotations of the Token in the 'toDraw' field
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
