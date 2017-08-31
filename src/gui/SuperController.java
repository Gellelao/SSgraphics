package src.gui;

import java.awt.Color;

import javax.swing.JOptionPane;

import src.model.Board;
import src.model.PlayerToken;
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

	public void attemptToSelect(TokenRegion r, String edge) {
		Token t = r.getToken();
		if(t == null)return;
		if(t.equals(selected)){
			if(edge != null)moveSelected(edge);
		}
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

	public void tokenSelect(Token t, PlayerToken p){
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
				JOptionPane.showMessageDialog(null, "There is a Piece occupying your spawn location\nYou must click the 'pass' button");
			}
			return;
		}
		if(p.toString().equals(myModel.getCurrent().toString())){
			selected = null;
			view.setSelectionPanelToken(t, p.toString());
			view.switchPlayerCard("Card with the four rotations of a token", p.toString());
		}
		else System.out.println("You can only click your pieces");
/*		if(p.getImage()[0].equals("1")){
			view.setSelectionPanelToken1(t);
			view.switchP1Card("Card with the four rotations of a token");
		}
		else {
			view.setSelectionPanelToken2(t);
			view.switchP2Card("Card with the four rotations of a token");
		}*/
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

	public void selectRotation(Token t){
		myModel.saveState();
		myModel.pushCommandHistory("create");
		myModel.spawnToken(t);
		phase++;
		updateMessage();
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
