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
	}
	
	public void tokenSelect(Token t, PlayerToken p){
		if(phase != 0) return;
		if(p.toString().equals(myModel.getCurrent().toString())){
			view.setSelectionPanelToken(t, p.toString());
			view.switchPlayerCard("Card with the four rotations of a token", p.toString());
		}
/*		if(p.getImage()[0].equals("1")){
			view.setSelectionPanelToken1(t);
			view.switchP1Card("Card with the four rotations of a token");
		}
		else {
			view.setSelectionPanelToken2(t);
			view.switchP2Card("Card with the four rotations of a token");
		}*/
	}
	
	public void selectRotation(Token t){
		myModel.saveState();
		myModel.pushCommandHistory("create");
		myModel.spawnToken(t);
		phase++;
	}
	
	public void selectDirection(String name, String direction){
		if(phase != 1)return;
		if(myModel.getCurrent().pieceIsChanged(name)){
			System.out.println("You have already changed that piece");
			return;
		}
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
