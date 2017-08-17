package src.gui;
import java.awt.*; import java.util.*; import javax.swing.*;

import src.model.Board;
import src.model.Token;

public class View extends JComponent implements Observer {
  private static final long serialVersionUID = 1L;
  Board myModel;
  public View(Board board) {
    myModel = board;
    myModel.addObserver(this);
    this.addKeyListener(new Controller(board));
    this.setFocusable(true);
    JFrame f = new JFrame("Wheat run, use (w,a,s,d) to play");
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.add(this);
    f.pack();
    f.setVisible(true);
  }

  public void paintComponent(Graphics _g) {
	    super.paintComponent(_g);
	    Graphics2D g = (Graphics2D) _g;
	    int minD = Math.min(getHeight(), getWidth());
	    g.setColor(Color.GREEN.darker());
	    g.fillRect(0, 0, minD, minD);
	    drawBoard(g);
  }

	/**
	 * Draws the board field of this Board object
	 */
	public void drawBoard(Graphics2D g) {
		Token[][] board = myModel.getBoard();
	    g.setColor(Color.BLUE);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
			    g.fillRect(100+i*10, 100+j*10, 8, 8);
			}
		}
/*		System.out.println(".............................................................");
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < board[0].length; k++) {
					System.out.print(":");
					if(board[i][k] == null)System.out.print("     ");
					else {
						String[] image = board[i][k].getImage();
						System.out.print(image[j]);
					}
				}
				System.out.print(":\n");
			}
			System.out.println(".............................................................");
		}*/
	}

	/**
	 * Draws the provided 2D array of token names
	 * The pieceNames map is used to find out what tokens to draw given their names
	 * Used when drawing a grid of tokens that is not the board, e.g the available tokens
	 *
	 * @param names - 2D array of token names to draw
	 * @param message - message to print at the top of the grid
	 */
	public void draw(String[][] names, String message) {
/*		Token[][] grid = new Token[names.length][names[0].length];
		// convert the token names into actual tokens
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				grid[i][j] = pieceNames.get(names[i][j]);
			}
		}

		System.out.print(message + "\n");
		for(int i = 0; i < 5*grid[0].length+grid[0].length+1; i++) {
			System.out.print(".");
		}
		System.out.print("\n");
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < grid[0].length; k++) {
					System.out.print(":");
					if(grid[i][k] == null)System.out.print("     ");
					else {
						String[] image = grid[i][k].getImage();
						System.out.print(image[j]);
					}
				}
				System.out.print(":\n");
			}
			for(int l = 0; l < 5*grid[0].length+grid[0].length+1; l++) {
				System.out.print(".");
			}
			System.out.print("\n");
		}*/
	}



/*  public void paintComponent(Graphics _g) {
    super.paintComponent(_g);
    Graphics2D g = (Graphics2D) _g;
    int minD = Math.min(getHeight(), getWidth());
    float d = minD / 20f;
    float step = minD / myModel.maxSize;
    g.setColor(Color.GREEN.darker());
    g.fillRect(0, 0, minD, minD);
    drawWheat(g, d, step);
    drawPigs(g, d, step);
    drawMonster(g, d, step);
  }
  private void drawPigs(Graphics2D g, float d, float step) {
    for (Pig p : myModel.getPigs()) {
      float px = p.getX() * step;
      float py = p.getY() * step;
      p.setBox(px, py);
      g.drawImage(ImgResources.PIG.img, (int) (px - d), (int) (py - d),
          (int) (d * 2), (int) (d * 2), null);
      g.setColor(Color.BLACK);
      g.draw(p.getBox());
    }
  }
  private void drawWheat(Graphics2D g, float d, float step) {
    float wx = myModel.getWheat().getX() * step;
    float wy = myModel.getWheat().getY() * step;
    myModel.getWheat().setBox(wx, wy);
    g.drawImage(ImgResources.WHEAT.img, (int) (wx - d), (int) (wy - d),
        (int) (d * 2), (int) (d * 2), null);

    g.setColor(Color.BLACK);
    g.draw(myModel.getWheat().getBox());
  }
  private void drawMonster(Graphics2D g, float d, float step) {
	  float mx = myModel.getMonster().getX() * step;
	  float my = myModel.getMonster().getY() * step;
	    myModel.getMonster().setBox(mx, my);
	    g.drawImage(ImgResources.MONSTER.img, (int) (mx - d), (int) (my - d),
	        (int) (d * 2), (int) (d * 2), null);

	    g.setColor(Color.BLACK);
	    g.draw(myModel.getMonster().getBox());
	  }*/


  public Dimension getPreferredSize() {return new Dimension(600, 600);}
  public void update(Observable arg0, Object arg1) {repaint();}
}