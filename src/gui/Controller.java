package src.gui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import src.model.Board;

public class Controller implements KeyListener, MouseListener {
	static Board myModel;
	private static Stack<String> commandHistory;

	public Controller(Board board) {
		myModel = board;
		commandHistory = new Stack<String>();
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w': return;
		case 'a': return;
		case 's': return;
		case 'd': return;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
	}

	*//**
	 * Calls the board undo method, and reverts the players lists to be accurate to the previous board state
	 *
	 * @return return the string describing the last action taken, so that the parser knows what to do next
	 *//*
	public static String undo(){
		if(!board.undo()) {
			System.out.println("There is nothing to undo");
			return "continue";
		}
		else{
			String lastCommand = commandHistory.pop();
			switch(lastCommand){
			case "create":
				currentPlayer.undoCreate();
				break;
			case "move":
				currentPlayer.undoChanges();
				break;
			case "rotate":
				currentPlayer.undoChanges();
				break;
			}
			return lastCommand;
		}
	}*/
}