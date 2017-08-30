package src.gui;

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

	public SuperController(Board model, View v, Controller boardControl, Controller p1Control, Controller p2Control, Controller s1Control, Controller s2Control) {
		this.view = v;
		myModel = model;

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

	public void attemptToSelect(Token t) {
		if(t == null)return;
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
	}

	public void selectRotation(Token t){
		myModel.saveState();
		myModel.pushCommandHistory("create");
		myModel.spawnToken(t);
		phase++;
		updateMessage();
	}

	public void selectDirection(String name, String direction){
		if(phase != 1){
			System.out.println("Not the correct phase");
			return;
		}
		if(myModel.getCurrent().pieceIsChanged(name)){
			System.out.println("You have already changed that piece");
			return;
		}
		myModel.moveToken(name, direction);
		myModel.getCurrent().changePiece(name);
	}

	public void pass(){
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

	/**
	 * Calls the board undo method, and reverts the players lists to be accurate to the previous board state
	 *
	 * @return return the string describing the last action taken, so that the parser knows what to do next
	 */
	public String undo(){
		if(!myModel.undo()) {
			System.out.println("There is nothing to undo");
			return "continue";
		}
		else{
			String lastCommand = myModel.popCommandHistory();
			switch(lastCommand){
			case "create":
				myModel.getCurrent().undoCreate();
				break;
			case "move":
				myModel.getCurrent().undoChanges();
				break;
			case "rotate":
				myModel.getCurrent().undoChanges();
				break;
			}
			return lastCommand;
		}
	}
}
