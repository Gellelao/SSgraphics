package src.gui;

import java.awt.Color;

import javax.swing.JOptionPane;

import src.model.Board;
import src.model.Player;
import src.model.Token;

public class SuperController {
	Board myModel;
	View view;
	Controller boardControl;
	Controller p1Control;
	Controller p2Control;
	Controller s1Control;
	Controller s2Control;
	BoardPanel b;
	int phase; // 0 = create, 1 = move
	Token selected = null;
	
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

	public Token getSelected() {
		return selected;
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
		if(t.equals(selected)){
			if(edge != null)moveSelected(edge);
		}

		int x = p1Control.getPanel().getWidth() + r.getX();
		int y = boardControl.getPanel().getRealY() + r.getY();
		
		animations.setCurrent(x, y, r.getSize(), r.getToken());
		
		Character c = t.toString().charAt(0);
		if(Character.isUpperCase(c)) {
			if(myModel.getCurrent().toString().equals("1")) {
				selected = t;
			}
		}
		else if(myModel.getCurrent().toString().equals("2")) {
			selected = t;
		}
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
			System.out.println("Not the correct phase");
			return;
		}
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
			// Animated the current player's panel:
			if(p.toString().equals("1"))p1Control.getPanel().shiftDown();
			else p2Control.getPanel().shiftDown();
			// Animating finished
			selected = null;
			view.setSelectionPanelToken(t, p.toString());
			view.switchPlayerCard("Card with the four rotations of a token", p.toString());
		}
		else System.out.println("You can only click your pieces");
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
		myModel.saveState();
		myModel.pushCommandHistory("create");
		
		// Animation Stuff starts
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
		// Animation stuff ends
		
		myModel.spawnToken(r.getToken());
		
		phase++;
		updateMessage();
	}

	public void updateMessage() {
		String playerName;
		String phaseName;
		if(myModel.getCurrent().toString().equals("1")) playerName = "Yellow";
			else playerName = "Green";
		if(phase == 0)phaseName = "Create";
			else phaseName = "Move";

		BoardPanel b = (BoardPanel)boardControl.getPanel();
		b.setMessage(playerName + "'s turn, " + phaseName + " phase.");
		myModel.notifyObs();
	}

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
		myModel.saveState();
		myModel.pushCommandHistory("move");
		
		// Animation stuff starts
		animations.setMove(direction);
		animations.animate();
		// ANimation stuff ends
		
		myModel.moveToken(name, direction);
		myModel.getCurrent().changePiece(name);
		stats.move(1);
		updateMessage();
	}

	public void pass(){
		view.setPassToShiny(false);
		beeper.resetError();
		System.out.println("pass");
		phase++;
		if(phase > 1){
			myModel.switchCurrent();
			phase = 0;
		}
		updateMessage();
		myModel.notifyObs();
	}
	
	public void setAnimations(AnimationPane anim){
		this.animations = anim;
	}

	public void setSidesToDefault(){
		view.switchPlayerCard("Card with all of a player's tokens", "1");
		view.switchPlayerCard("Card with all of a player's tokens", "2");
	}
	
	public StatTracker getStats() {return stats;}

	/**
	 * Calls the board undo method, and reverts the players lists to be accurate to the previous board state
	 *
	 * @return return the string describing the last action taken, so that the parser knows what to do next
	 */
	public void undo(){
		stats.undo();
		
		if(phase == 0){
			phase = 1;
			myModel.switchCurrent();
			updateMessage();
			return;
		}
		
		myModel.undo();

		String lastCommand = myModel.popCommandHistory();
		if(lastCommand == null)return;
		switch(lastCommand){
		case "create":
			myModel.getCurrent().undoCreate();
			phase = 0;
			break;
		case "move":
			stats.move(-1);
			myModel.getCurrent().undoChanges();
			break;
		case "rotate":
			myModel.getCurrent().undoChanges();
			break;
		}
		updateMessage();
	}
}
