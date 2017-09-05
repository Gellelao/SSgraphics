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
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import src.model.Board;
import src.model.Token;
import src.model.Token;

/**
 * The 'View' part of the MVC system.
 * This constructs all the required panels and arranges them. It creates all the menu buttons
 * and sets their actions. It also deals with switching between panels, using CardLayouts
 * 
 * @author Deacon
 *
 */
public class View extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	
	private Board myModel;
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

	public View(Board board) {

		myModel = board;
		myModel.addObserver(this);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(0, 0, screenWidth, screenHeight);

		// Cards is a panel containing the 'menu' and 'game' cards, which are switched between when needed.
		cards = new JPanel(new CardLayout());
		cards.setFocusable(false);

		// Make the menu panel
		JPanel menuPanel = new JPanel(new BorderLayout());
		JPanel menuButtons = new JPanel();
		
				// Make the 'Play' button
				JButton play = new JButton("Begin New Game");
				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchToCard(cards, GAMEPANEL);
					}});
				play.setFocusable(false);
				
				// Make the 'Info' button
				JButton info = new JButton("Info");
				info.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Made by Deacon McIntyre, 2017");
					}});
				info.setFocusable(false);
				
				// Make the 'Quit' button
				JButton quit = new JButton("Quit");
				quit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(1);
					}});
				quit.setFocusable(false);
		
		// Add the buttons to a sub-panel
		menuButtons.add(play);
		menuButtons.add(info);
		menuButtons.add(quit);
		// Add the sub-panel to the menu
		menuPanel.add(menuButtons, BorderLayout.NORTH);
		
		// Add the game title to the menu
		menuPanel.add(new JLabel("Sword and Shield Game", JLabel.CENTER), BorderLayout.CENTER);

		menuButtons.setFocusable(false);
		menuPanel.setFocusable(false);
		
				// Make the 'Undo' button
				JButton undo = new JButton("Undo");
				undo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						supControl.undo();
				}});
				undo.setFocusable(false);
				
				// Make the 'Pass' button
				pass = new JButton("Pass");
				pass.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						supControl.pass();
				}});
				pass.setFocusable(false);
				
				// Make the 'Surrender' button
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
		
		// Make a sub-panel to hold the buttons
		JPanel buttonRow = new JPanel();

		// Add the buttons to the sub-panel
		buttonRow.add(undo);
		buttonRow.add(pass);
		buttonRow.add(surrender);
		buttonRow.setFocusable(false);

		// Create the panels that make up the main game
		// These use the classes that extend AbstractGamePanel
		CemeteryPanel g1 =         new CemeteryPanel(myModel);
		BoardPanel boardPanel =    new BoardPanel(myModel);
		PlayerPanel playerPanel1 = new PlayerPanel(myModel, "p1");
		PlayerPanel playerPanel2 = new PlayerPanel(myModel, "p2");
		selectionPanel1 =          new TokenSelectionPanel(myModel, "p1");
		selectionPanel2 =          new TokenSelectionPanel(myModel, "p2");

		// The yellow and green panels on the side are both CardLayout panels,
		// which contain a playerPanel and a TokenSelectionPanel
		p1 = new JPanel(new CardLayout());
			p1.add(playerPanel1, PLAYERTOKENPANEL);
			p1.add(selectionPanel1, TOKENSELECTIONPANEL);
		p2 = new JPanel(new CardLayout());
			p2.add(playerPanel2, PLAYERTOKENPANEL);
			p2.add(selectionPanel2, TOKENSELECTIONPANEL);

		// Create the almighty SuperController, now that we have the main panels (which have initialized their own controllers)
		supControl = new SuperController(myModel, this, boardPanel.getController(), playerPanel1.getController(), playerPanel2.getController(), selectionPanel1.getController(), selectionPanel2.getController());

		// Combine the main panels in splits, so they are all resizeable
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boardPanel, g1);
		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, middleSplit);
		JSplitPane allThreeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftAndCenterSplit, p2);

		// Make the splits look nice
		leftAndCenterSplit.setResizeWeight(0.33);
		allThreeSplit.setResizeWeight(0.75);
		middleSplit.setResizeWeight(0.75);

		// Make a 'game' panel to hold the final split-pane and the game buttons
		JPanel game = new JPanel(new BorderLayout());
		
		// Add the split-pane and button row to the 'game' panel
		game.add(buttonRow, BorderLayout.NORTH);
		game.add(allThreeSplit, BorderLayout.CENTER);

		// Add the menu and the game to the cards panel
		cards.add(menuPanel, MENUPANEL);
		cards.add(game, GAMEPANEL);
		cards.setBounds(0, 0, screenWidth, screenHeight);
		
		
		//=========================
		// Set up animation panels:
		//=========================
		// Create a layeredPane in order to animate things across the screen
		JLayeredPane layers = new JLayeredPane();
		layers.setBounds(0, 0, screenWidth, screenHeight);
		
		// Set up the custom AnimationPane and give it to the SuperController
		AnimationPane animationLayer = new AnimationPane();
		supControl.setAnimations(animationLayer);

		// Add the 'cards' panel into the layeredPane, along with the animationPane
		layers.add(cards, 1);
		layers.add(animationLayer, 0);

		animationLayer.setOpaque(false);
		animationLayer.setBounds(0, 0, screenWidth, screenHeight);
		animationLayer.validate();
	
		// Finally, add the layeredPanel to this View (View extends JFrame)
		this.add(layers);
		this.setVisible(true);
	}

	/**
	 * Sets the token which will be shown in four rotations
	 * 
	 * @param t - the token to show
	 * @param player - The owner of the panel which will be changed
	 */
	public void setSelectionPanelToken(Token t, String player){
		if(player.equals("1"))
			selectionPanel1.setTokenToDraw(t);
		else selectionPanel2.setTokenToDraw(t);
	}

	/**
	 * Switches the given player's panel to the card identified by String s
	 * 
	 * @param s - the Card identifier string
	 * @param player - The player who owns the panel that will be acted on
	 */
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
	
	/**
	 * Sets the pass button to be bright yellow, or sets it to default, depending on the boolean
	 * 
	 * @param on - if true, the pass button becomes yellow, otherwise default
	 */
	public void setPassToShiny(boolean on) {
		if(on) {
			pass.setForeground(new Color(165, 163, 38));
			pass.setBackground(Color.YELLOW);
		}
		else {
			pass.setForeground(Color.BLACK);
			// Just a way to get the default colour of buttons
			pass.setBackground(new JButton().getBackground());
		}
	}

	/**
	 * A general method for switching between cards in a cardLayout
	 * 
	 * @param p - the Panel with a CardLayout to change
	 * @param s - the string identifying the card to change to
	 */
	public void switchToCard(JPanel p, String s){
		CardLayout layout = (CardLayout) p.getLayout();
		layout.show(p, s);
	}

	public Dimension getPreferredSize() {return new Dimension(screenWidth, screenHeight);}   //0, 0);}
	public void update(Observable arg0, Object arg1) {repaint();}
}
