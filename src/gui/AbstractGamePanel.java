package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import src.model.Token;

public abstract class AbstractGamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public void drawBoard(Graphics2D g, Token[][] grid) {
		int borderWeight = 4;
		int tokenSize = 32;
		
		int boardWidth = (tokenSize+borderWeight)*grid.length+borderWeight;
		int boardHeight = (tokenSize+borderWeight)*grid[0].length+borderWeight;
		// These "real" coords are based on the current size of the JPanel
		int realX = getWidth()/2-boardWidth/2;
		int realY = getHeight()/2-boardHeight/2;

		// Draw the board grid background
		g.setColor(Color.BLACK);
		g.fillRect(realX, realY, boardWidth, boardHeight);
		// Draw each tile's background
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				int x = realX+borderWeight+(i*(tokenSize+borderWeight));
				int y = realY+borderWeight+(j*(tokenSize+borderWeight));
				g.setColor(Color.GRAY);
				g.fillRect(x, y, tokenSize, tokenSize);
				if(i == 2 && j == 2 && grid[i][j] == null) {
					g.setColor(Color.YELLOW.darker());
					g.fillRect(x, y, tokenSize, tokenSize);
				}
				if(i == 7 && j == 7 && grid[i][j] == null) {
					g.setColor(Color.GREEN.darker());
					g.fillRect(x, y, tokenSize, tokenSize);
				}
				if(grid[i][j] != null) {
					drawToken(g, x, y, 32, grid[i][j]);
				}
			}
		}
	}
	
	private void drawToken(Graphics2D g, int x, int y, int size, Token p){
		String[] tokenInfo = p.getImage();
		if(tokenInfo[0].equals("1")) {
			g.setColor(Color.YELLOW.brighter());
			g.fillRect(x, y, size, size);
			return;
		}
		else if(tokenInfo[0].equals("2")) {
			g.setColor(Color.GREEN.brighter());
			g.fillRect(x, y, size, size);
			return;
		}
		else if(tokenInfo[0].toUpperCase().equals(tokenInfo[0])) {
			g.setColor(Color.GREEN);
		}
		else g.setColor(Color.YELLOW);
		g.fillOval(x, y, size, size);
		// Draw the red swords and shields
		
	}
/*	public void paintComponent(Graphics _g) {
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		g.setColor(Color.GREEN.darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		drawBoard(g, myModel.getBoard());
	}*/
}
