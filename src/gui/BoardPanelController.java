package src.gui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import src.model.Board;
import src.model.Token;

public class BoardPanelController implements Controller, KeyListener, MouseListener {
	private Board myModel;
	// Maybe move this commandHistroy to the board and manage it there
	private Stack<String> commandHistory;
	private BoardPanel panel;
	Token selected = null;
	private SuperController superC;

	public BoardPanelController(Board board, BoardPanel panel) {
		myModel = board;
		commandHistory = new Stack<String>();
		this.panel = panel;
	}

	public void keyPressed(KeyEvent e) {
		System.out.println("KEYPressed");}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		System.out.println("KEYTYPED");
		if(selected == null)return;
		switch (e.getKeyChar()) {
		case 'w': System.out.println("W"); return;//myModel.moveToken(selected.toString(), "up");  // TODO: Note the name is used here not the token - may cause issues
		case 'a': myModel.moveToken(selected.toString(), "left"); return;
		case 's': myModel.moveToken(selected.toString(), "down"); return;
		case 'd': myModel.moveToken(selected.toString(), "right"); return;
		}
	}
	
	@Override
	public AbstractGamePanel getPanel(){
		return panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(selected == null)selected = panel.getToken(mouseX, mouseY);
		//else panel.checkForEdgeClick(selected);
		panel.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// Convert player inputs into these methods below:

/*	public static void createPiece(String letter, int angle){
		board.saveState();
		commandHistory.push("create");
		board.addToken(letter, currentPlayer.getSpawnX(), currentPlayer.getSpawnY());
		board.rotateToken(letter, angle);
		currentPlayer.removeFromAvailable(letter);
		currentPlayer.addToPlayed(letter);
	}

	public static void movePiece(String letter, String direction){
		board.saveState();
		commandHistory.push("move");
		board.moveToken(letter, direction);
	}

	private static void rotatePiece(String letter, int angle){
		board.saveState();
		commandHistory.push("rotate");  // + angle
		board.rotateToken(letter, angle);
	}*/

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
			String lastCommand = commandHistory.pop();
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

	@Override
	public void setSuper(SuperController s) {
		this.superC = s;
	}
}