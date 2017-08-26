package src.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*; import javax.swing.*;

import src.model.Board;

public class BoardPanel extends AbstractGamePanel implements Observer {
	private static final long serialVersionUID = 1L;
	private Board myModel;
	private JFrame current;
	private JPanel cards;
	final static String MENUPANEL = "Card with menu buttons";
	final static String GAMEPANEL = "Card with the game contents";

	final static int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	final static int screenHeight= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

	BoardPanelController control;

	public BoardPanel(Board board) {


		myModel = board;
		myModel.addObserver(this);

		control = new BoardPanelController(board, this);
		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(true);

		// "current" is the single JFrame that this program uses
		current = new JFrame("Sword and Shield Game");
		current.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		current.setBounds(0, 0, screenWidth, screenHeight);

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
		JPanel buttonRow = new JPanel();
		JButton undo = new JButton("Undo");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				control.undo();
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

		buttonRow.add(undo);
		buttonRow.add(pass);
		buttonRow.add(surrender);

		PlayerPanel p1 = new PlayerPanel(myModel, "p1");
		PlayerPanel p2 = new PlayerPanel(myModel, "p2");
		CemeteryPanel g1 = new CemeteryPanel(myModel);

		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, this);
		JSplitPane allThreeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftAndCenterSplit, p2);
		JSplitPane finalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, allThreeSplit, g1);

		leftAndCenterSplit.setResizeWeight(0.25);
		allThreeSplit.setResizeWeight(0.75);
		finalSplit.setResizeWeight(0.75);

		game.add(finalSplit, BorderLayout.CENTER);
		game.add(buttonRow, BorderLayout.NORTH);
		//game.add(new GamePanel(), BorderLayout.SOUTH);
		//game.add(p1, BorderLayout.EAST);
		//game.add(p2, BorderLayout.WEST);

		// Add the two panels to the cards panel
		cards.add(menuButtons, MENUPANEL);
		cards.add(game, GAMEPANEL);

		current.add(cards);
		//current.pack();
		current.setVisible(true);
	}

	@Override
	protected void drawAll(Graphics2D g) {
		super.drawGrid(g, myModel.getBoard());
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


  public Dimension getPreferredSize() {return new Dimension(screenWidth, screenHeight);}
  public void update(Observable arg0, Object arg1) {repaint();}

}