package src.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import src.model.Board;
import src.model.Token;

public class View extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	private Board myModel;
	//private JFrame current;
	private JPanel cards;
	private JPanel p1;
	private JPanel p2;
	final static String MENUPANEL = "Card with menu buttons";
	final static String GAMEPANEL = "Card with the game contents";
	final static String PLAYERTOKENPANEL = "Card with all of a player's tokens";
	final static String TOKENSELECTIONPANEL = "Card with the four rotations of a token";

	final static int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	final static int screenHeight= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

	private SuperController supControl;
	private TokenSelectionPanel selectionPanel1;
	private TokenSelectionPanel selectionPanel2;
	private JButton pass;

	String message = "Welcome";

	public View(Board board) {

		myModel = board;
		myModel.addObserver(this);

		// "current" is the single JFrame that this program uses
		//current = new JFrame("Sword and Shield Game");
		//current.setFocusable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(0, 0, screenWidth, screenHeight);

		cards = new JPanel(new CardLayout());
		cards.setFocusable(false);

		// Make the menu panel
		JPanel menuPanel = new JPanel(new BorderLayout());
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
					JOptionPane.showMessageDialog(null, "Made by Deacon McIntyre, 2017");
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
		menuPanel.add(menuButtons, BorderLayout.NORTH);
		menuPanel.add(new JLabel("Sword and Shield Game", JLabel.CENTER), BorderLayout.CENTER);

		menuButtons.setFocusable(false);
		menuPanel.setFocusable(false);


		// Make the game panel
		JPanel game = new JPanel(new BorderLayout());
		JPanel buttonRow = new JPanel();
		JButton undo = new JButton("Undo");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					supControl.undo();
			}});
			undo.setFocusable(false);
		pass = new JButton("Pass");
			pass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					supControl.pass();
			}});
			pass.setFocusable(false);
		JButton surrender = new JButton("Surrender");
			surrender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String playerName;
					if(myModel.getCurrent().toString().equals("1")){
						playerName = "Green";
					}
					else playerName = "Yellow";

					JOptionPane.showMessageDialog(null, playerName + " Player Wins!");

					StatTracker s = supControl.getStats();
					JOptionPane.showMessageDialog(null, "Stats: \n\n"
							+ "Moves:  " + s.getMoves() + "\n"
							+ "Undos:  " + s.getUndos() + "\n"
							+ "Time:   " + s.getTime() + " seconds\n"
							+ "Deaths: " + s.getDeaths() + "\n");
					switchToCard(cards, MENUPANEL);
			}});
			surrender.setFocusable(false);

		buttonRow.add(undo);
		buttonRow.add(pass);
		buttonRow.add(surrender);
		buttonRow.setFocusable(false);

		BoardPanel boardPanel = new BoardPanel(myModel);
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
		supControl = new SuperController(myModel, this, boardPanel.getController(), playerPanel1.getController(), playerPanel2.getController(), selectionPanel1.getController(), selectionPanel2.getController());

		p1.setFocusable(false);
		p2.setFocusable(false);
		playerPanel1.setFocusable(false);
		playerPanel2.setFocusable(false);
		selectionPanel1.setFocusable(false);
		selectionPanel2.setFocusable(false);

		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boardPanel, g1);
		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, middleSplit);
		JSplitPane allThreeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftAndCenterSplit, p2);

		leftAndCenterSplit.setResizeWeight(0.33);
		allThreeSplit.setResizeWeight(0.75);
		middleSplit.setResizeWeight(0.75);

		//JLabel turnInfo = new JLabel("Sword and Shield Game", JLabel.CENTER), BorderLayout.CENTER);

		game.add(buttonRow, BorderLayout.NORTH);
		//game.add(new JLabel(message, JLabel.CENTER), BorderLayout.NORTH);
		game.add(allThreeSplit, BorderLayout.CENTER);
		//game.add(new GamePanel(), BorderLayout.SOUTH);
		//game.add(p1, BorderLayout.EAST);
		//game.add(p2, BorderLayout.WEST);

		// Add the two panels to the cards panel
		cards.add(menuPanel, MENUPANEL);
		cards.add(game, GAMEPANEL);
		cards.setBounds(0, 0, screenWidth, screenHeight);
		
		JLayerPanel anims = new JLayered 

		this.add(cards);
		this.setVisible(true);
	}

	public void setSelectionPanelToken(Token t, String player){
		if(player.equals("1"))
			selectionPanel1.setTokenToDraw(t);
		else selectionPanel2.setTokenToDraw(t);
	}

	public void switchPlayerCard(String s, String player){
		if(player.equals("1")){
			CardLayout layout = (CardLayout) p1.getLayout();
			layout.show(p1, s);
		}
		else {
			CardLayout layout = (CardLayout) p2.getLayout();
			layout.show(p2, s);
		}
	}
	
	public void setPassToShiny(boolean on) {
		if(on) {
			pass.setForeground(new Color(165, 163, 38));
			pass.setBackground(Color.YELLOW);
		}
		else {
			pass.setForeground(Color.BLACK);
			pass.setBackground(new JButton().getBackground());
		}
	}

/*	public void setSelectionPanelToken1(Token t){
		selectionPanel1.setTokenToDraw(t);
	}

	public void setSelectionPanelToken2(Token t){
		selectionPanel2.setTokenToDraw(t);
	}*/

	public void switchToCard(JPanel p, String s){
		CardLayout layout = (CardLayout) p.getLayout();
		layout.show(p, s);
	}

/*	public void switchP1Card(String s){
		CardLayout layout = (CardLayout) p1.getLayout();
		layout.show(p1, s);
	}

	public void switchP2Card(String s){
		CardLayout layout = (CardLayout) p2.getLayout();
		layout.show(p2, s);
	}*/

	public Dimension getPreferredSize() {return new Dimension(screenWidth, screenHeight);}   //0, 0);}
	 public void update(Observable arg0, Object arg1) {repaint();}
}
