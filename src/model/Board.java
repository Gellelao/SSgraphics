package src.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Stack;

import src.model.Player;

/**
 * The 'Model' part of MVC.
 * Class that takes care of the 2d array of tokens called the board
 * This class is responsible for creating and moving tokens around, undoing the board, and storing players and the cemetery.
 * An object of this class can be asked for information about either of the players, which player
 * is taking their turn, and information about the board or cemetery
 *
 * @author Deacon
 *
 */
public class Board extends Observable{
	private Token[][] board;
	// Maps Token names to the Tokens themselves. This way other classes only need to deal with names.
	// This is possible because each token has a unique name (Letters a-x for player 1, letters A-X for player 2)
	private HashMap<String, Token> pieceNames;
	private Stack<Token[][]> history;
	private Stack<ArrayList<String>> cmHistory;
	private ArrayList<String> cemetery;
	private Stack<String> commandHistory;

	Player one;
	Player two;
	Player current;

	public Board(){
		board = new Token[10][10];
		history = new Stack<Token[][]>();
		pieceNames = new HashMap<String, Token>();
		cmHistory = new Stack<ArrayList<String>>();
		cemetery = new ArrayList<String>();
		commandHistory = new Stack<String>();

		one = new Player("1");
		two = new Player("2");
		current = one;

		// I hid the huge wall of token initializations at the bottom
		initialiseMap();
	}

	/**
	 * Gets the Tokens that a given player has yet to play, in a 2D array format
	 * 
	 * @param p - the player to get Tokens from
	 * @return - a 2D array of that player's available Tokens
	 */
	public Token[][] getAvailable(Player p) {
		ArrayList<String> names = (ArrayList<String>) p.getAvailable();
		String[][] nameArray = ListToGrid(names, 3, 8);
		Token[][] tokens = namesToTokens(nameArray);
		return tokens;
	}

	/**
	 * Gets all possible(4) rotations of Token t
	 * 
	 * @param t - the token to rotate
	 * @return - an array of the rotations
	 */
	public Token[][] getRotations(Token t){
		Token[][] rotations = new Token[4][1];
		rotations[0][0] = t;
		Token t2 = t.clone();
		t2.rotate();
		rotations[1][0] = t2;
		Token t3 = t2.clone();
		t3.rotate();
		rotations[2][0] = t3;
		Token t4 = t3.clone();
		t4.rotate();
		rotations[3][0] = t4;
/*		for(int i = 0; i < 4; i++){
			rotations[i][0] = t;
			t.rotate();
		}*/
		return rotations;
	}

	/**
	 * Is provided with a token and puts it into the board
	 * Automatically creates in the spawn tile of the current player
	 * so does not need creation coordinates
	 *
	 * @param t - the token to spawn
	 */
	public void spawnToken(Token t) {
		if(!currentSpawnOccupied()){
			board[current.getSpawnX()][current.getSpawnY()] = t;

			current.removeFromAvailable(t.toString());
			current.addToPlayed(t.toString());

	    	notifyObs();
		}
	}
	
	/**
	 * Move the given token in the given direction, making sure to push other tokens ahead,
	 * and to send tokens to the cemetery if they go into forbidden zones
	 *
	 * @param name the name of the token to move
	 * @param dir the direction to move the token in
	 */
	public void moveToken(String name, String dir) {
		int row = getTokenRow(name);
		int col = getTokenCol(name);

		switch(dir){
		case("up"):
			if(col-1 < 0){
				cemetery.add(board[row][col].toString());
				board[row][col] = null;
			}
			else{
				if(board[row][col-1] != null){
					moveToken(board[row][col-1].toString(), "up");
				}
				board[row][col-1] = board[row][col];
				board[row][col] = null;
			}
			break;
		case("down"):
			if(col+1 >= board[0].length){
				cemetery.add(board[row][col].toString());
				board[row][col] = null;
			}
			else{
				if(board[row][col+1] != null){
					moveToken(board[row][col+1].toString(), "down");
				}
				board[row][col+1] = board[row][col];
				board[row][col] = null;
			}
			break;
		case("left"):
			if(row-1 < 0){
				cemetery.add(board[row][col].toString());
				board[row][col] = null;
			}
			else{
				if(board[row-1][col] != null){
					moveToken(board[row-1][col].toString(), "left");
				}
				board[row-1][col] = board[row][col];
				board[row][col] = null;
			}
			break;
		case("right"):
			if(row+1 >= board.length){
				cemetery.add(board[row][col].toString());
				board[row][col] = null;
			}
			else{
				if(board[row+1][col] != null){
					moveToken(board[row+1][col].toString(), "right");
				}
				board[row+1][col] = board[row][col];
				board[row][col] = null;
			}
			break;
		}
		// Remove any tokens in the eight forbidden corner tiles
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(board[i][j] != null && !board[i][j].toString().equals("1")){
					cemetery.add(board[i][j].toString());
					board[i][j] = null;
				}
			}
		}
		for(int i = 8; i < 10; i++) {
			for(int j = 8; j < 10; j++) {
				if(board[i][j] != null && !board[i][j].toString().equals("2")){
					cemetery.add(board[i][j].toString());
					board[i][j] = null;
				}
			}
		}

		// This tells the view to redraw (I believe)
		notifyObs();
	}

	public Token[][] getBoard(){return board;}
	public Player getP1() {return one;}
	public Player getP2() {return two;}
	public Player getCurrent() {return current;}

	/**
	 * @return - the cemetery, in 2D array form
	 */
	public Token[][] getCemetery() {
		String[][] nameArray = ListToGrid(cemetery, 14, 4);
		Token[][] tokens = namesToTokens(nameArray);
		return tokens;
	}

	public int cemeterySize() {return cemetery.size();}

	/**
	 * @return true if the spawn tile of the current player is occupied, otherwise false
	 */
	public boolean currentSpawnOccupied() {
		return board[current.getSpawnX()][current.getSpawnY()] != null;
	}

	/**
	 * Changes the current player
	 */
	public void switchCurrent() {
		if(current.equals(one)) {
			current = two;
		}
		else current = one;
		one.clearChangedPieces();
		two.clearChangedPieces();
	}

	/**
	 * @return - the top of the commandHistory stack, or null if the stack is empty
	 */
	public String popCommandHistory(){
		if(commandHistory.isEmpty())return null;
		return commandHistory.pop();
	}

	/**
	 * @param s - the string to push onto the commandHistory
	 */
	public void pushCommandHistory(String s){
		commandHistory.push(s);
	}

	/**
	 * Save the board and the cemetery into their designated stacks
	 */
	public void saveState() {
		Token[][] temp = new Token[board.length][board[0].length];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != null)
					temp[i][j] = board[i][j].clone();
			}
		}
		history.push(temp);
		cmHistory.push((ArrayList<String>) cemetery.clone());
	}

	/**
	 * Pop the latest copy of the board off the history stack
	 * and replace the current board with that
	 */
	public void undo() {
		if(cmHistory.isEmpty())return;
		if(history.isEmpty())return;

		cemetery = cmHistory.pop();

		Token[][] latestSave = history.pop();
		for(int i = 0; i < latestSave.length; i++) {
			for(int j = 0; j < latestSave[0].length; j++) {
				board[i][j] = latestSave[i][j];
			}
		}
		notifyObs();
	}

	/**
	 * Notifies observers and calls setChanged()
	 */
	public void notifyObs(){
		setChanged();
    	notifyObservers();
	}

	public int getTokenRow(String name){
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != null && board[i][j].toString().equals(name))return i;
			}
		}
		System.out.println("getTokenRow() has not found piece \"" + name + "\"");
		return -1;
	}

	public int getTokenCol(String name){
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != null && board[i][j].toString().equals(name))return j;
			}
		}
		System.out.println("getTokenCol() has not found piece \"" + name + "\"");
		return -1;
	}

	/**
	 * Converts a list of strings into an array of string with the given height and width
	 *
	 * @param list - the list of string to convert
	 * @param height - the height of the array
	 * @param width - the width of the array
	 * @return - the 2D array of Strings created form the list
	 */
	private String[][] ListToGrid(ArrayList<String> list, int height, int width){
		String[][] grid = new String[height][width];
		int cumulative = 0;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(cumulative < list.size())
					grid[i][j] = list.get(cumulative);
				cumulative++;
			}
		}
		return grid;
	}
	
	/**
	 * Converts a 2D array of String into a 2D array of Tokens, using the strings as Token names
	 * 
	 * @param names - the array of strings to convert
	 * @return - the array of Tokens
	 */
	private Token[][] namesToTokens(String[][] names){
		Token[][] tokens = new Token[names.length][names[0].length];
		// convert the token names into actual tokens
		for(int i = 0; i < names.length; i++) {
			for(int j = 0; j < names[0].length; j++) {
				tokens[i][j] = pieceNames.get(names[i][j]);
			}
		}
		return tokens;
	}

	private void initialiseMap() {
		pieceNames.put("a", new Token("a", 1, 2, 1, 1));
		pieceNames.put("b", new Token("b", 0, 0, 0, 0));
		pieceNames.put("c", new Token("c", 1, 2, 1, 2));
		pieceNames.put("d", new Token("d", 1, 0, 1, 2));
		pieceNames.put("e", new Token("e", 1, 0, 1, 0));
		pieceNames.put("f", new Token("f", 1, 0, 1, 1));
		pieceNames.put("g", new Token("g", 1, 2, 2, 1));
		pieceNames.put("h", new Token("h", 1, 2, 0, 1));
		pieceNames.put("i", new Token("i", 1, 0, 2, 1));
		pieceNames.put("j", new Token("j", 1, 0, 0, 1));
		pieceNames.put("k", new Token("k", 2, 2, 2, 2));
		pieceNames.put("l", new Token("l", 1, 1, 1, 1));
		pieceNames.put("m", new Token("m", 1, 0, 0, 0));
		pieceNames.put("n", new Token("n", 1, 0, 0, 2));
		pieceNames.put("o", new Token("o", 1, 2, 0, 0));
		pieceNames.put("p", new Token("p", 1, 0, 2, 0));
		pieceNames.put("q", new Token("q", 1, 0, 2, 2));
		pieceNames.put("r", new Token("r", 1, 2, 2, 0));
		pieceNames.put("s", new Token("s", 1, 2, 0, 2));
		pieceNames.put("t", new Token("t", 1, 2, 2, 2));
		pieceNames.put("u", new Token("u", 0, 2, 0, 0));
		pieceNames.put("v", new Token("v", 0, 2, 2, 0));
		pieceNames.put("w", new Token("w", 0, 2, 0, 2));
		pieceNames.put("x", new Token("x", 0, 2, 2, 2));

		pieceNames.put("A", new Token("A", 1, 2, 1, 1));
		pieceNames.put("B", new Token("B", 0, 0, 0, 0));
		pieceNames.put("C", new Token("C", 1, 2, 1, 2));
		pieceNames.put("D", new Token("D", 1, 0, 1, 2));
		pieceNames.put("E", new Token("E", 1, 0, 1, 0));
		pieceNames.put("F", new Token("F", 1, 0, 1, 1));
		pieceNames.put("G", new Token("G", 1, 2, 2, 1));
		pieceNames.put("H", new Token("H", 1, 2, 0, 1));
		pieceNames.put("I", new Token("I", 1, 0, 2, 1));
		pieceNames.put("J", new Token("J", 1, 0, 0, 1));
		pieceNames.put("K", new Token("K", 2, 2, 2, 2));
		pieceNames.put("L", new Token("L", 1, 1, 1, 1));
		pieceNames.put("M", new Token("M", 1, 0, 0, 0));
		pieceNames.put("N", new Token("N", 1, 0, 0, 2));
		pieceNames.put("O", new Token("O", 1, 2, 0, 0));
		pieceNames.put("P", new Token("P", 1, 0, 2, 0));
		pieceNames.put("Q", new Token("Q", 1, 0, 2, 2));
		pieceNames.put("R", new Token("R", 1, 2, 2, 0));
		pieceNames.put("S", new Token("S", 1, 2, 0, 2));
		pieceNames.put("T", new Token("T", 1, 2, 2, 2));
		pieceNames.put("U", new Token("U", 0, 2, 0, 0));
		pieceNames.put("V", new Token("V", 0, 2, 2, 0));
		pieceNames.put("W", new Token("W", 0, 2, 0, 2));
		pieceNames.put("X", new Token("X", 0, 2, 2, 2));
	}
}
