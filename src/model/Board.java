package src.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Stack;

import src.model.Player;

/**
 * Class that takes care of the 2d array of tokens called the board
 * This class is responsible for moving tokens around, drawing the board, and undoing the board
 *
 * @author Deacon
 *
 */
public class Board extends Observable{
	private Token[][] board;
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
		//addToken(one, 1, 1);
		//addToken(two, 8, 8);

		current = one;

		initialiseMap();
	}

	public Token[][] getBoard(){return board;}

	public Player getP1() {return one;}
	public Player getP2() {return two;}

	public Token[][] getAvailable(Player p) {
		ArrayList<String> names = (ArrayList<String>) p.getAvailable();
		String[][] nameArray = ListToGrid(names, 3, 8);
		Token[][] tokens = namesToTokens(nameArray);
		return tokens;
	}

	public Token[][] getCemetery() {
		String[][] nameArray = ListToGrid(cemetery, 14, 4);
		Token[][] tokens = namesToTokens(nameArray);
		return tokens;
	}

	public Player getCurrent() {
		return current;
	}

	public void switchCurrent() {
		if(current.equals(one)) {    // TODO: if problems with player turns not switching, might be because this equals isn't comparing correctly
			current = two;
		}
		else current = one;
		one.clearChangedPieces();
		two.clearChangedPieces();
	}

	public String popCommandHistory(){
		if(commandHistory.isEmpty())return null;
		return commandHistory.pop();
	}

	public void pushCommandHistory(String s){
		commandHistory.push(s);
	}

	public int cemeterySize() {return cemetery.size();}

	/**
	 * Converts a list of strings into an array of string with the given height and width
	 *
	 * @param list
	 * @param height
	 * @param width
	 * @return
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

	public Token[][] getRotations(Token t){
		Token[][] rotations = new Token[4][1];
		rotations[0][0] = t;
		Token t2 = t.copy();
		t2.rotate();
		rotations[1][0] = t2;
		Token t3 = t2.copy();
		t3.rotate();
		rotations[2][0] = t3;
		Token t4 = t3.copy();
		t4.rotate();
		rotations[3][0] = t4;
/*		for(int i = 0; i < 4; i++){
			rotations[i][0] = t;
			t.rotate();
		}*/
		return rotations;
	}

	public void notifyObs(){
		setChanged();
    	notifyObservers();
	}

	public boolean currentSpawnOccupied() {
		return board[current.getSpawnX()][current.getSpawnY()] != null;
	}

	/**
	 * Is provided with a token and puts it into the board
	 * Automatically creates in the spawn tile of the current player
	 * so does not need creation coordinates
	 *
	 * @param t token
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
	 * Is provided with a token and puts it into the board
	 * Does not draw the board afterwards because this is currently only used by
	 * the TextUI class which provides players to add
	 *
	 * @param t token
	 * @param x
	 * @param y
	 */
	public void addToken(Token t, int x, int y) {
		board[x][y] = t;
	}

	/**
	 * Is given the name of a token rather than a token object
	 * Fetches the actual token that matches this name and put it into the board
	 * Redraws the board afterwards
	 *
	 * The player class uses this the most so that players don't store lists of tokens -
	 * just their names instead
	 *
	 * @param name name of token
	 * @param x
	 * @param y
	 */
	public void addToken(String name, int x, int y) {
		Token p = pieceNames.get(name);
		p.setToDefault();
		board[x][y] = p;
	}

	public void rotateToken(String name, int angle) {
		Token t = getToken(name);
		for(int i = 0; i < angle/90; i++) {
			t.rotate();
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
		//System.out.println("moving "+ dir);

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
					cemetery.add(board[i][j].toString());// Remove this line for a different set of bugs
					board[i][j] = null;
				}
			}
		}
		for(int i = 8; i < 10; i++) {
			for(int j = 8; j < 10; j++) {
				if(board[i][j] != null && !board[i][j].toString().equals("2")){
					cemetery.add(board[i][j].toString());// Remove this line for a different set of bugs
					board[i][j] = null;
				}
			}
		}

		notifyObs();
	}

	public void printCemetery(){
		String[][] names = new String[5][10];
		String message = " ________\n/Cemetery\\";
		int cumulative = 0;
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 10; j++) {
				if(cumulative < cemetery.size())
					names[i][j] = cemetery.get(cumulative);
				cumulative++;
			}
		}
	}

	/**
	 * Save the board and the cemetery into their designated stacks
	 */
	public void saveState() {
		Token[][] temp = new Token[board.length][board[0].length];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != null)
					temp[i][j] = board[i][j].copy();
			}
		}
		history.push(temp);
		cmHistory.push((ArrayList<String>) cemetery.clone());
	}

	/**
	 * Pop the latest copy of the board off the history stack
	 * and replace the current board with that
	 *
	 * @return returns true if success, false if history is empty
	 */
	public void undo() {
		if(cmHistory.isEmpty())return;
		if(history.isEmpty())return;

		cemetery = cmHistory.pop();

		Token[][] latestSave = history.pop();
		for(int i = 0; i < latestSave.length; i++) {
			for(int j = 0; j < latestSave[0].length; j++) {

				/*if(latestSave[i][j] == null)board[i][j] = null;
				else board[i][j] = latestSave[i][j].copy();*/

				board[i][j] = latestSave[i][j];
			}
		}
		notifyObs();
	}

	public Token getToken(int x, int y){
		return board[x][y];
	}

	public Token getToken(String name){
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != null && board[i][j].toString().equals(name))return board[i][j];
			}
		}
		System.out.println("getToken() has not found piece \"" + name + "\"");
		return null;
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
