package src.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*; import javax.swing.*;

import src.model.Board;
import src.model.Token;

public class View extends JComponent implements Observer {
	private static final long serialVersionUID = 1L;
	private Board myModel;
	private JFrame current;
	private JPanel cards;
	final static String MENUPANEL = "Card with menu buttons";
	final static String GAMEPANEL = "Card with the game contents";

	public View(Board board) {


		myModel = board;
		myModel.addObserver(this);
		this.addKeyListener(new Controller(board));
		this.setFocusable(true);

		// "current" is the single JFrame that this program uses
		current = new JFrame("Sword and Shield Game");
		current.setBounds(700, 200, 500, 500);

		cards = new JPanel(new CardLayout());

		// Make the menu panel
		JPanel menuButtons = new JPanel();
		JButton play = new JButton("Begin New Game");
			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CardLayout layout = (CardLayout) cards.getLayout();
					layout.show(cards, GAMEPANEL);
				}});
		JButton info = new JButton("Info");
			info.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					current.add(new JLabel("Made by Deacon, 2017"));
				}});
		JButton quit = new JButton("Quit");
			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(1);
				}});
		menuButtons.add(play);
		menuButtons.add(info);
		menuButtons.add(quit);

		// Make the game panel
		JPanel game = new JPanel(new BorderLayout());
		game.add(this, BorderLayout.CENTER);
		JPanel bottomRow = new JPanel();
		JButton undo = new JButton("Undo");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				// undo method
			}});
		JButton pass = new JButton("Pass");
			pass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				// pass method
			}});
		JButton surrender = new JButton("Surrender");
			surrender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CardLayout layout = (CardLayout) cards.getLayout();
					layout.show(cards, MENUPANEL);
			}});

		bottomRow.add(undo);
		bottomRow.add(pass);
		bottomRow.add(surrender);

		game.add(new JPanel(), BorderLayout.NORTH);
		game.add(bottomRow, BorderLayout.SOUTH);
		game.add(new JPanel(), BorderLayout.EAST);
		game.add(new JPanel(), BorderLayout.WEST);

		// Add the two panels to the cards panel
		cards.add(menuButtons, MENUPANEL);
		cards.add(game, GAMEPANEL);

		current.add(cards);
		//current.pack();
		current.setVisible(true);
	}

/*	public void play() {
		JFrame f = new JFrame("Sword and Shield Game");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		f.add(this, BorderLayout.CENTER);
		JPanel s = new JPanel();
			s.add(new JButton("Undo"));
			s.add(new JButton("Pass"));
			s.add(new JButton("Surrender"));
		f.add(new JPanel(), BorderLayout.NORTH);
		f.add(s, BorderLayout.SOUTH);
		f.add(new JPanel(), BorderLayout.EAST);
		f.add(new JPanel(), BorderLayout.WEST);
		f.pack();
		f.setVisible(true);

		current = f;
		current.pack();
		current.setVisible(true);
	}*/

	public void printInfo() {

	}

	public void paintComponent(Graphics _g) {
		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;
		g.setColor(Color.GREEN.darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		drawBoard(g, myModel.getBoard());
	}

	/**
	 * Draws the board field of this Board object
	 */
	public void drawBoard(Graphics2D g, Token[][] grid) {
		int borderGap = 50;
		int borderWeight = 5;
		int tokenSize = 32;

		// Draw the board grid background
		g.setColor(Color.BLACK);
		g.fillRect(borderGap, borderGap,
				(tokenSize+borderWeight)*grid.length+borderWeight,
				(tokenSize+borderWeight)*grid[0].length+borderWeight);
		g.setColor(Color.GRAY);
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				g.fillRect(borderGap+borderWeight+(i*(tokenSize+borderWeight)),
						   borderGap+borderWeight+(j*(tokenSize+borderWeight)),
						   tokenSize, tokenSize);
			}
		}
	}

	private void drawToken(int x, int y, Token p){
		if(p instanceof src.model.PlayerToken) {

		}
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


  public Dimension getPreferredSize() {return new Dimension(1200, 800);}
  public void update(Observable arg0, Object arg1) {repaint();}
}