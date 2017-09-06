package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import src.model.Token;

/**
 * Each different panel type in the game extends this class, because they all require some form of
 * grid or token drawing.
 * 
 * @author Deacon
 *
 */
public abstract class AbstractGamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	int borderWeight;
	int tokenSize;

	int realX;
	int realY;
	
	// Fields used for animating this panel
	int currentY;
	boolean moving = false;

	Token[][] grid;

	/**
	 * Draws a 2D grid of any dimensions, with black borders
	 * Scales to the panel size
	 * 
	 * @param g - graphics object
	 * @param gridParam - The grid to draw
	 */
	public void drawGrid(Graphics2D g, Token[][] gridParam) {
		this.grid = gridParam;

		borderWeight = Math.min(getWidth(), getHeight())/100;
		tokenSize    = Math.min(getWidth(), getHeight())/10 - Math.min(getWidth(), getHeight())/50;

		int boardWidth = (tokenSize+borderWeight)*grid.length+borderWeight;
		int boardHeight = (tokenSize+borderWeight)*grid[0].length+borderWeight;

		// These "real" coords are based on the current size of the JPanel
		realX = getWidth()/2-boardWidth/2;
		realY = getHeight()/2-boardHeight/2;

		// Draw the board grid background (This results in the black borders)
		g.setColor(Color.BLACK);
		// The 'moving' boolean is used by the sliding animation, which means a different 'y' location is used
		if(moving) g.fillRect(realX, currentY, boardWidth, boardHeight);
		else g.fillRect(realX, realY, boardWidth, boardHeight);
		
		// Draw each tile
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				int x = realX+borderWeight+(i*(tokenSize+borderWeight));
				int y;
				if(moving) y = currentY+borderWeight+(j*(tokenSize+borderWeight));
				else       y = realY+borderWeight+(j*(tokenSize+borderWeight));

				// Add "tokenRegions" to make click detection easier
				addRegion(new TokenRegion(grid[i][j], x, y, tokenSize));
				
				// Rules differ between panels - the board is the biggest difference as it has to draw different coloured tiles
				applyRules(g, i, j, x, y);
			}
		}
	}
	
	// These are used the find out where to draw the "flying" tokens when animating
	public int getRealX(){return realX;}
	public int getRealY(){return realY;}
	public int getTokenSize(){return tokenSize;}

	/**
	 * Draws a token, detects which colour(yellow/green),
	 * and draws the red "swords" and "shields"
	 * 
	 * @param g - Graphics object
	 * @param x - x ordinate of top left of the token
	 * @param y - y ordinate of top left of the token
	 * @param size - width/heigh of the token (They will always be squares)
	 * @param p - the Token to draw
	 */
	protected void drawToken(Graphics2D g, int x, int y, int size, Token p){
		String[] tokenInfo = p.getImage();
		
		// Token colour depends on player - one player uses uppercase token names
		// so these are used to identify the player controlling the token
		if(tokenInfo[0].toUpperCase().equals(tokenInfo[0])) {
			g.setColor(Color.YELLOW);
		}
		else g.setColor(Color.GREEN);
		g.fillOval(x, y, size, size);

		// Draw the red swords and shields
		g.setColor(Color.RED);
		int barWidth = size/7;
		
		if(tokenInfo[1].equals("1")) {
			// North Sword
			g.fillRect(x + size/2-(barWidth/2), y, barWidth, size/2);
		}
		if(tokenInfo[1].equals("2")) {
			// North Shield
			g.fillRect(x, y, size, barWidth);
		}
		if(tokenInfo[2].equals("1")) {
			// East Sword
			g.fillRect(x+size/2, y + size/2-(barWidth/2), size/2, barWidth);
		}
		if(tokenInfo[2].equals("2")) {
			// East Shield
			g.fillRect(x+size-barWidth, y, barWidth, size);
		}
		if(tokenInfo[3].equals("1")) {
			// South Sword
			g.fillRect(x + size/2-(barWidth/2), y+size/2, barWidth, size/2);
		}
		if(tokenInfo[3].equals("2")) {
			// South Shield
			g.fillRect(x, y+size-barWidth, size, barWidth);
		}
		if(tokenInfo[4].equals("1")) {
			// West Sword
			g.fillRect(x, y + size/2-(barWidth/2), size/2+(barWidth/2), barWidth);
		}
		if(tokenInfo[4].equals("2")) {
			// West Shield
			g.fillRect(x, y, barWidth, size);
		}
	}

	/**
	 * @return the background colour for tiles drawn by drawGrid(). Default is gray
	 */
	protected Color getTileColour() {
		return Color.GRAY.darker();
	}

	protected abstract void drawAll(Graphics2D g);

	protected abstract Color getBGColour();

	public abstract Controller getController();
	
	public abstract void addRegion(TokenRegion r);

	
	/**
	 * Most panels that extends this class use this default implementation of applyRules,
	 * because they only need to draw the tokens and nothing else. The boardPanel overrides this.
	 * 
	 * @param g - graphics object
	 * @param i - the row of the grid to use
	 * @param j - the column of the grid to use
	 * @param x - the x coordinate to draw to
	 * @param y - the y coordinate to draw to
	 */
	protected void applyRules(Graphics g, int i, int j, int x, int y) {
		g.setColor(getTileColour());
		g.fillRect(x, y, tokenSize, tokenSize);
		
		if(grid[i][j] != null) {
			drawToken((Graphics2D) g, x, y, tokenSize, grid[i][j]);
		}
	}
	
	/**
	 * Animates this panel, making it slide down off the bottom of the screen
	 */
	protected void shiftDown(){
		anchorCurrentY();
		moving = true;
		while(currentY < this.getHeight()){
			currentY += 4;
			paintImmediately(0,0, getWidth(), getHeight());
		}
		moving = false;
	}
	
	/**
	 * Resets the currentY to be the default position of the grid
	 */
	private void anchorCurrentY(){
		currentY = realY;
	}

	public void paintComponent(Graphics _g) {
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		drawAll(g);
	}
}
