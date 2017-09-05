package src.gui;

import javax.swing.JOptionPane;
import src.model.Board;
import src.model.Player;
import src.model.Token;

/**
 * The 'Controller' part of MVC.
 * This is the class which interprets user actions into game actions.
 * It is sent information about user clicks or key presses by its Controller fields
 * 
 * @author Deacon
 *
 */
public class SuperController {
	Board myModel;
	View view;
	Controller boardControl;
	Controller p1Control;
	Controller p2Control;
	Controller s1Control;
	Controller s2Control;

	int phase; // 0 = create, 1 = move
	Token selected = null;
	
	// Accessories
	SoundSystem beeper;
	StatTracker stats;
	AnimationPane animations;

	public SuperController(Board model, View v, Controller boardControl, Controller p1Control, Controller p2Control, Controller s1Control, Controller s2Control) {
		this.view = v;
		myModel = model;
		beeper = new SoundSystem();
		stats = new StatTracker(myModel);

		phase = 0;

		this.boardControl = boardControl;
		this.p1Control = p1Control;
		this.p2Control = p2Control;
		this.s1Control = s1Control;
		this.s2Control = s2Control;

		boardControl.setSuper(this);
		p1Control.setSuper(this);
		p2Control.setSuper(this);
		s1Control.setSuper(this);
		s2Control.setSuper(this);
		updateMessage();
	}

	/**
	 * Called by the boardPanel's controller when the player clicks on a token
	 * Takes a TokenRegion parameter because, like the "selectRotation()" method below, this is method
	 * is responsible for some animations, and needs the information from a TokenRegion.
	 * This method also interprets the action of the player clicking the edge of a token to move it.
	 * This is done using the edge parameter, calculated before being passed in here.
	 * 
	 * @param r - The TokenRegion 'underneath' the mouse click
	 * @param edge - the edge of the token that has been clicked (0=N, 1=E, 2=S, 3=W), or NULL is no edge has been clicked
	 */
	public void boardSelect(TokenRegion r, String edge) {
		Token t = r.getToken();
		if(t == null)return;
		
		// If the user has clicked an edge, move in that direction
		if(t.equals(selected)){
			if(edge != null)moveSelected(edge);
		}

		// Animations start
		int x = p1Control.getPanel().getWidth() + r.getX();
		int y = boardControl.getPanel().getRealY() + r.getY();
		
		animations.setCurrent(x, y, r.getSize(), r.getToken());
		// Animations end
		
		// If the user has not clicked an edge, attempt to select that token.
		// The token will only be selected if it belong to the player taking their turn
		Character c = t.toString().charAt(0);
		if(Character.isUpperCase(c)) {
			if(myModel.getCurrent().toString().equals("1")) {
				selected = t;
			}
			else beeper.playBeep();
		}
		else if(myModel.getCurrent().toString().equals("2")) {
			selected = t;
		}
		else beeper.playBeep();
	}

	/**
	 * Called by the playerPanel controller - ensures that the player is following the rules before switching
	 * to the tokenSelectionPanel behind, to allow the player to select a rotation
	 * 
	 * @param t - the token that has been clicked
	 * @param p - the player owning the token
	 */
	public void playerSelect(Token t, Player p){
		if(phase != 0){
			beeper.playBeep();
			System.out.println("Not the correct phase");
			return;
		}
		// Warn the player about their mistake
		if(myModel.currentSpawnOccupied()) {
			System.out.println("Your spawn tile is occupied");
			beeper.increaseError();
			if(beeper.getError() == 2) {
				view.setPassToShiny(true);
			}
			if(beeper.getError() > 2) {
				JOptionPane.showMessageDialog(null, "There is a Token occupying your spawn location\nYou must click the 'pass' button");
			}
			return;
		}
		// If the player who owns the clicked token is the player taking their turn, continue.
		if(p.toString().equals(myModel.getCurrent().toString())){
			
			// Animate the current player's panel:
			if(p.toString().equals("1"))p1Control.getPanel().shiftDown();
			else p2Control.getPanel().shiftDown();
			// Animation ends
			
			selected = null;
			// Switch to a panel with which the player can select a token rotation
			view.setSelectionPanelToken(t, p.toString());
			view.switchPlayerCard("Card with the four rotations of a token", p.toString());
		}
		else {
			beeper.playBeep();
			System.out.println("You can only click your pieces");
		}
	}
	
	/**
	 * Called by the TokenSelectionPanel controller - it first animates the token that has been clicked
	 * - this is why it takes a TokenRegion instead of a Token - it needs the information about the location
	 * for where to animate from.
	 * Then it spawn the token for the current player, using the model (Board), and moves this SuperController to the next phase
	 * 
	 * @param r - The regions that the mouse has been clicked in - carries information about the origin of the token clicked, and
	 * stores the token itself.
	 */
	public void selectRotation(TokenRegion r){
		// Before spawning anything, save the state and remember the last command
		myModel.saveState();
		myModel.pushCommandHistory("create");
		
		// Animations start
		AbstractGamePanel toAnimate;
		
		Character c = r.getToken().toString().charAt(0);
		if(Character.isUpperCase(c)){
			animations.setCurrent(r);
			toAnimate = s1Control.getPanel();
		}
		else{
			int x = p1Control.getPanel().getWidth() + boardControl.getPanel().getWidth() + r.getX();
			int y = r.getY();
			animations.setCurrent(x, y, r.getSize(), r.getToken());
			toAnimate = s2Control.getPanel();
		}
		
		// For some reason each player needed a different number when calculating the target y
		// Luckily these numbers happened to be the player's names:                      |
		int offset = Integer.parseInt(myModel.getCurrent().toString());//                v
		
		int y = boardControl.getPanel().getRealY() + (myModel.getCurrent().getSpawnY()+offset) * boardControl.getPanel().getTokenSize();
		int x = p1Control.getPanel().getWidth() + boardControl.getPanel().getRealX();
		x += (myModel.getCurrent().getSpawnX()+1) * boardControl.getPanel().getTokenSize();
		
		animations.setTarget(x, y);
		animations.animate();
		
		toAnimate.shiftDown();
		// Animations end
		
		myModel.spawnToken(r.getToken());
		
		phase++;
		updateMessage();
	}

	/**
	 * Changes the little message above the board to read who's turn it is and what phase they are in
	 */
	public void updateMessage() {
		String playerName;
		String phaseName;
		if(myModel.getCurrent().toString().equals("1")) playerName = "Yellow";
			else playerName = "Green";
		
		if(phase == 0)phaseName = "Create";
			else phaseName = "Move";

		BoardPanel b = (BoardPanel)boardControl.getPanel();
		b.setMessage(playerName + "'s turn, " + phaseName + " phase.");
		// Redraw
		myModel.notifyObs();
	}

	/**
	 * Tells the model to move the selected token in the direction provided. The selected token is kept
	 * track of using a field in this class.
	 * 
	 * @param direction
	 */
	public void moveSelected(String direction){
		if(selected == null)return;
		String name = selected.toString();
		
		if(phase != 1){
			System.out.println("Not the correct phase");
			return;
		}
		if(myModel.getCurrent().pieceIsChanged(name)){
			System.out.println("You have already changed that piece");
			return;
		}
		
		// Save state and remember last command before changing the board
		myModel.saveState();
		myModel.pushCommandHistory("move");
		
		// Animation stuff starts
		animations.setMove(direction);
		animations.animate();
		// ANimation stuff ends
		
		// Move the token and update the current player so that their information is correct
		myModel.moveToken(name, direction);
		myModel.getCurrent().changePiece(name);
		
		// Update the statistics and the message
		stats.move(1);
		updateMessage();
	}

	/**
	 * Called whenever the 'pass' button is clicked. Increments the phase, or changes to the next turn
	 * if passing would end the current turn
	 */
	public void pass(){
		// Reset the error system
		view.setPassToShiny(false);
		beeper.resetError();

		phase++;
		if(phase > 1){
			myModel.switchCurrent();
			phase = 0;
		}
		
		// Update and redraw
		updateMessage();
		myModel.notifyObs();
	}

	/**
	 * Calls the model's undo method, and reverts the players lists to be accurate to the previous board state
	 * Also attempts to change back to the previous turn if undo is clicked at the start of a turn 
	 * 
	 */
	public void undo(){
		stats.undo();
		
		// If in create phase and if there is something to undo
		if(phase == 0 && myModel.popCommandHistory() != null){
			// Change to move phase of previous turn
			phase = 1;
			myModel.switchCurrent();
			updateMessage();
			return;
		}
		myModel.undo();

		String lastCommand = myModel.popCommandHistory();
		if(lastCommand == null)return;
		switch(lastCommand){
		
		// If undoing a create, switch to the create phase
		case "create":
			myModel.getCurrent().undoCreate();
			phase = 0;
			break;
			
		// If undoing a move, update decrease the amount of moves in the statistics
		case "move":
			stats.move(-1);
			myModel.getCurrent().undoChanges();
			break;
			
		// Rotations currently not supported during turns
		case "rotate":
			myModel.getCurrent().undoChanges();
			break;
		}
		updateMessage();
	}

	/**
	 *  Makes the side panels both show the players tokens, rather than the four rotations panel
	 */
	public void setSidesToDefault(){
		view.switchPlayerCard("Card with all of a player's tokens", "1");
		view.switchPlayerCard("Card with all of a player's tokens", "2");
	}
	
	public void setAnimations(AnimationPane anim){
		this.animations = anim;
	}

	public Token getSelected() {
		return selected;
	}
	
	public StatTracker getStats() {return stats;}
}
