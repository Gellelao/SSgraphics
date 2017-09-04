package src.gui;
import java.awt.*;
import java.util.*;
import src.model.Board;
import src.model.Token;

/**
 * Panel representing the playing board. This panel sits in the center of the screen
 * It uses the myModel field to get the grid of tokens it needs to draw, using the draw
 * method from AbstractGamePanel which this extends
 * 
 * @author Deacon
 *
 */
public class BoardPanel extends AbstractGamePanel {
	private static final long serialVersionUID = 1L;

	private Board myModel;
	private BoardPanelController control;
	private ArrayList<TokenRegion> regions;
	String message = "Welcome";

	public BoardPanel(Board model){
		myModel = model;
		control = new BoardPanelController(myModel, this);
		regions = new ArrayList<TokenRegion>();

		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(true);
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
				myModel.notifyObs();
				return r;
			}
		}
		return null;
	}

	/**
	 * Sets the message that will be displayed at the top of the board
	 * 
	 * @param m
	 */
	public void setMessage(String m) {
		this.message = m;
	}

	/**
	 * Fills the background of the board panel, and draws the grid onto it
	 */
	@Override
	protected void drawAll(Graphics2D g) {
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		g.drawString(message, 20, 20);

		regions.clear();
		//
	   // Here is the main real point of difference between panel versions:
	   // This panel uses myModel.getBoard() as the grid to draw, other panels use other grids
		//
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
		// Important for the keyListener to work:
		this.requestFocusInWindow();
	}

	/**
	 * Checks the given i and j to see if a special tile should be drawn, e.g. the player's faces or spawn tiles
	 * Also draws the checkers. Other panels do not have this implementation of this method because they do not
	 * need any special tiles
	 */
	@Override
	protected void applyRules(Graphics g, int i, int j, int x, int y) {
		// Checkers:
		if((i+j)%2 != 1) {
			g.setColor(getTileColour().brighter());
		}
		else g.setColor(getTileColour().darker());
		g.fillRect(x, y, tokenSize, tokenSize);

		// Faces:
		if((i == 1 && j == 1)||(i == 8 && j == 8)){
			if(i == 1) g.setColor(Color.YELLOW.brighter());
			else g.setColor(Color.GREEN.brighter());
			
			g.fillRect(x, y, tokenSize, tokenSize);
			
			int third = tokenSize/3;
			int eyeSize = tokenSize/8;
			g.setColor(Color.BLACK);
			g.fillOval(x+third-(eyeSize/2), y+third, eyeSize, eyeSize);
			g.fillOval(x+third+third-(eyeSize/2), y+third, eyeSize, eyeSize);
			g.fillArc(x+third+eyeSize/4, y+third+third, third, third/2, 45, 90);
			return;
		}
		
		// Other special tiles:
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
		
		// Draw token:
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