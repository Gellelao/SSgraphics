package src.gui;

import src.model.Board;
import src.model.PlayerToken;
import src.model.Token;

public class SuperController {
	Board myModel;
	Controller boardControl;
	Controller p1Control;
	Controller p2Control;
	Controller s1Control;
	Controller s2Control;
	BoardPanel b;
	String state;

	public SuperController(Board model, Controller boardControl, Controller p1Control, Controller p2Control, Controller s1Control, Controller s2Control) {
		b = (BoardPanel) boardControl.getPanel();
		myModel = model;
		
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
		System.out.println(p.getImage()[0]);
		if(p.getImage()[0].equals("1")){
			b.setSelectionPanelToken1(t);
			b.switchP1Card("Card with the four rotations of a token");
		}
		else {
			b.setSelectionPanelToken2(t);
			b.switchP2Card("Card with the four rotations of a token");
		}
	}
	
	public void selectRotation(Token t){
		myModel.spawnToken(t);
	}
	
	public void setSidesToDefault(){
		b.switchP1Card("Card with all of a player's tokens");
		b.switchP2Card("Card with all of a player's tokens");
	}
}
