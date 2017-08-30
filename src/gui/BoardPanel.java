package src.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*; import javax.swing.*;

import src.model.Board;
import src.model.Token;

public class BoardPanel extends AbstractGamePanel {
	private static final long serialVersionUID = 1L;
	private Board myModel;
	private BoardPanelController control;
	private ArrayList<TokenRegion> regions;
	private Token selected;
	
	public BoardPanel(Board model){
		myModel = model;
		control = new BoardPanelController(myModel, this);
		regions = new ArrayList<TokenRegion>();
		

		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(true);
	}
	
	public Token getToken(int x, int y){
		for(TokenRegion r : regions){
			if(r.contains(x, y)){
				selected = r.getToken();
				return r.getToken();
			}
		}
		return null;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		regions.clear();
		super.drawGrid(g, myModel.getBoard());
		
		if(selected != null){
			for(TokenRegion r : regions){
				if(r.getToken() == null)continue;
				if(r.getToken().toString().equals(selected.toString())){
					r.show(g);
				}
			}
		}
		this.requestFocusInWindow();
	}

	@Override
	protected void applyRules(Graphics g, int i, int j, int x, int y) {
		if((i+j)%2 != 1) {
			g.setColor(getTileColour().brighter());
		}
		else g.setColor(getTileColour().darker());
		g.fillRect(x, y, tokenSize, tokenSize);

		if(i == 2 && j == 2 && grid[i][j] == null) {
			g.setColor(Color.YELLOW.darker());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(i == 7 && j == 7 && grid[i][j] == null) {
			g.setColor(Color.GREEN.darker());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(i == 0 && j < 2) {
			g.setColor(getBGColour());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(i < 2 && j == 0) {
			g.setColor(getBGColour());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(i == 9 && j > 7) {
			g.setColor(getBGColour());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(i > 7 && j == 9) {
			g.setColor(getBGColour());
			g.fillRect(x, y, tokenSize, tokenSize);
		}
		if(grid[i][j] != null) {
			super.drawToken((Graphics2D) g, x, y, tokenSize, grid[i][j]);
		}
	}

	@Override
	protected Color getBGColour() {
		return new Color(36, 62, 122);
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