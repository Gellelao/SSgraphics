package src.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*; import javax.swing.*;

import src.model.Board;
import src.model.Token;

public class BoardPanel extends AbstractGamePanel implements Observer {
	private static final long serialVersionUID = 1L;
	private Board myModel;
	private JFrame current;
	private JPanel cards;
	private JPanel p1;
	private JPanel p2;
	final static String MENUPANEL = "Card with menu buttons";
	final static String GAMEPANEL = "Card with the game contents";
	final static String PLAYERTOKENPANEL = "Card with all of a player's tokens";
	final static String TOKENSELECTIONPANEL = "Card with the four rotations of a token";

	final static int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	final static int screenHeight= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

	private BoardPanelController control;
	private SuperController supControl;
	private TokenSelectionPanel selectionPanel1;
	private TokenSelectionPanel selectionPanel2;
	private ArrayList<TokenRegion> regions;

	public BoardPanel(Board board) {
		regions = new ArrayList<TokenRegion>();

		myModel = board;
		myModel.addObserver(this);

		control = new BoardPanelController(board, this);
		this.addKeyListener(control);
		this.addMouseListener(control);
		this.setFocusable(true);
		// "current" is the single JFrame that this program uses
		current = new JFrame("Sword and Shield Game");
		current.setFocusable(false);
		current.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		current.setBounds(0, 0, screenWidth, screenHeight);

		cards = new JPanel(new CardLayout());
		cards.setFocusable(false);

		// Make the menu panel
		JPanel menuButtons = new JPanel();
		JButton play = new JButton("Begin New Game");
			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchToCard(cards, GAMEPANEL);
				}});
			play.setFocusable(false);
		JButton info = new JButton("Info");
			info.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					current.add(new JLabel("Made by Deacon, 2017"));
				}});
			info.setFocusable(false);
		JButton quit = new JButton("Quit");
			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(1);
				}});
			quit.setFocusable(false);
		menuButtons.add(play);
		menuButtons.add(info);
		menuButtons.add(quit);
		menuButtons.setFocusable(false);

		// Make the game panel
		JPanel game = new JPanel(new BorderLayout());
		JPanel buttonRow = new JPanel();
		JButton undo = new JButton("Undo");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				control.undo();
			}});
			undo.setFocusable(false);
		JButton pass = new JButton("Pass");
			pass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				// pass method
			}});
			pass.setFocusable(false);
		JButton surrender = new JButton("Surrender");
			surrender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchToCard(cards, MENUPANEL);
			}});
			surrender.setFocusable(false);

		buttonRow.add(undo);
		buttonRow.add(pass);
		buttonRow.add(surrender);
		buttonRow.setFocusable(false);

		PlayerPanel playerPanel1 = new PlayerPanel(myModel, "p1");
		PlayerPanel playerPanel2 = new PlayerPanel(myModel, "p2");
		selectionPanel1 = new TokenSelectionPanel(myModel, "p1");
		selectionPanel2 = new TokenSelectionPanel(myModel, "p2");
		
		p1 = new JPanel(new CardLayout());
			p1.add(playerPanel1, PLAYERTOKENPANEL);
			p1.add(selectionPanel1, TOKENSELECTIONPANEL);
		p2 = new JPanel(new CardLayout());
			p2.add(playerPanel2, PLAYERTOKENPANEL);
			p2.add(selectionPanel2, TOKENSELECTIONPANEL);
			
		CemeteryPanel g1 = new CemeteryPanel(myModel);
		supControl = new SuperController(myModel, control, playerPanel1.getController(), playerPanel2.getController(), selectionPanel1.getController(), selectionPanel2.getController());
		
		p1.setFocusable(false);
		p2.setFocusable(false);
		playerPanel1.setFocusable(false);
		playerPanel2.setFocusable(false);
		selectionPanel1.setFocusable(false);
		selectionPanel2.setFocusable(false);
		
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this, g1);
		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, middleSplit);
		JSplitPane allThreeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftAndCenterSplit, p2);

		leftAndCenterSplit.setResizeWeight(0.33);
		allThreeSplit.setResizeWeight(0.75);
		middleSplit.setResizeWeight(0.75);

		game.add(allThreeSplit, BorderLayout.CENTER);
		game.add(buttonRow, BorderLayout.NORTH);
		//game.add(new GamePanel(), BorderLayout.SOUTH);
		//game.add(p1, BorderLayout.EAST);
		//game.add(p2, BorderLayout.WEST);

		// Add the two panels to the cards panel
		cards.add(menuButtons, MENUPANEL);
		cards.add(game, GAMEPANEL);

		current.add(cards);
		//current.pack();
		this.requestFocusInWindow();
		current.setVisible(true);
	}
	
	public void setSelectionPanelToken1(Token t){
		selectionPanel1.setTokenToDraw(t);
	}
	
	public void setSelectionPanelToken2(Token t){
		selectionPanel2.setTokenToDraw(t);
	}
	
	public void switchToCard(JPanel p, String s){
		CardLayout layout = (CardLayout) p.getLayout();
		layout.show(p, s);
	}
	
	public void switchP1Card(String s){
		CardLayout layout = (CardLayout) p1.getLayout();
		layout.show(p1, s);
	}
	
	public void switchP2Card(String s){
		CardLayout layout = (CardLayout) p2.getLayout();
		layout.show(p2, s);
	}
	
	public Token getToken(int x, int y){
		for(TokenRegion r : regions){
			if(r.contains(x, y))return r.getToken();
		}
		return null;
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
	
	@Override
	public void addRegion(TokenRegion r) {
		regions.add(r);
	}


  public Dimension getPreferredSize() {return new Dimension(screenWidth, screenHeight);}   //0, 0);}
  public void update(Observable arg0, Object arg1) {repaint();}

}