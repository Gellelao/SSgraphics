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
	String message = "Welcome";

	public BoardPanel(Board model){
		myModel = model;
		control = new BoardPanelController(myModel, this);
		regions = new ArrayList<TokenRegion>();


		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(true);
	}

	public TokenRegion getRegion(int x, int y){
		for(TokenRegion r : regions){
			if(r.contains(x, y)){
				myModel.notifyObs();
				return r;
			}
		}
		return null;
	}

	public void setMessage(String m) {
		this.message = m;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString(message, 20, 20);

		regions.clear();
		super.drawGrid(g, myModel.getBoard());

		Token selected = control.getSuperSelected();

		if(selected != null){
			for(TokenRegion r : regions){
				if(r.getToken() == null)continue;
				if(r.getToken().toString().equals(selected.toString())){
					r.show(g);
				}
			}
		}
		// Important:
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
			// Draw indicator to show piece has been changed this turn
			if(myModel.getCurrent().pieceIsChanged(grid[i][j].toString())){
				int size = tokenSize/3;
				int extra = tokenSize/20;
				g.setColor(Color.BLACK);
				g.fillRect(x+extra/2, y+extra/2, size+extra, size+extra);
				g.setColor(Color.WHITE);
				g.fillRect(x+extra, y+extra, size, size);
				g.setColor(Color.BLACK);
				g.drawLine(x+extra, y+extra, x+size+extra, y+size+extra);
				g.drawLine(x+size+extra, y+extra, x+extra, y+size+extra);
			}
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
		if(r.getToken() == null)return;
		if(r.getToken().toString().matches("[a-xA-X]"))
			regions.add(r);
	}




}