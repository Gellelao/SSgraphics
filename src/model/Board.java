package src.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Stack;

import src.model.PlayerToken;

/**
 * Class that takes care of the 2d array of tokens called the board
 * This class is responsible for moving tokens around, drawing the board, and undoing the board
 *
 * @author Deacon
 *
 */
public class Board extends Observable{
	private Token[][] board;
	private HashMap<String, PieceToken> pieceNames;
	private Stack<Token[][]> history;
	private Stack<ArrayList<String>> cmHistory;
	private ArrayList<String> cemetery;
	private Stack<String> commandHistory;

	PlayerToken one;
	PlayerToken two;
	PlayerToken current;

	public Board(){
		board = new Token[10][10];
		history = new Stack<Token[][]>();
		pieceNames = new HashMap<String, PieceToken>();
		cmHistory = new Stack<ArrayList<String>>();
		cemetery = new ArrayList<String>();
		commandHistory = new Stack<String>();

		one = new PlayerToken("1");
		two = new PlayerToken("2");
		addToken(one, 1, 1);
		addToken(two, 8, 8);

		current = one;

		initialiseMap();
	}

	public Token[][] getBoard(){return board;}

	public PlayerToken getP1() {return one;}
	public PlayerToken getP2() {return two;}

	public Token[][] getAvailable(PlayerToken p) {
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

	public PlayerToken getCurrent() {
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
		return commandHistory.pop();
	}

	public void pushCommandHistory(String s){
		commandHistory.push(s);
	}

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
		PieceToken p = pieceNames.get(name);
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
			if(col+1 > board[0].length){
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
			if(row+1 > board.length){
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
	public boolean undo() {
		if(!cmHistory.isEmpty()){
			cemetery = cmHistory.pop();
		}
		if(!history.isEmpty()) {
			Token[][] latestSave = history.pop();
			for(int i = 0; i < latestSave.length; i++) {
				for(int j = 0; j < latestSave[0].length; j++) {
					board[i][j] = latestSave[i][j];
				}
			}
			return true;
		}
		return false;
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
		pieceNames.put("a", new PieceToken("a", 1, 2, 1, 1));
		pieceNames.put("b", new PieceToken("b", 0, 0, 0, 0));
		pieceNames.put("c", new PieceToken("c", 1, 2, 1, 2));
		pieceNames.put("d", new PieceToken("d", 1, 0, 1, 2));
		pieceNames.put("e", new PieceToken("e", 1, 0, 1, 0));
		pieceNames.put("f", new PieceToken("f", 1, 0, 1, 1));
		pieceNames.put("g", new PieceToken("g", 1, 2, 2, 1));
		pieceNames.put("h", new PieceToken("h", 1, 2, 0, 1));
		pieceNames.put("i", new PieceToken("i", 1, 0, 2, 1));
		pieceNames.put("j", new PieceToken("j", 1, 0, 0, 1));
		pieceNames.put("k", new PieceToken("k", 2, 2, 2, 2));
		pieceNames.put("l", new PieceToken("l", 1, 1, 1, 1));
		pieceNames.put("m", new PieceToken("m", 1, 0, 0, 0));
		pieceNames.put("n", new PieceToken("n", 1, 0, 0, 2));
		pieceNames.put("o", new PieceToken("o", 1, 2, 0, 0));
		pieceNames.put("p", new PieceToken("p", 1, 0, 2, 0));
		pieceNames.put("q", new PieceToken("q", 1, 0, 2, 2));
		pieceNames.put("r", new PieceToken("r", 1, 2, 2, 0));
		pieceNames.put("s", new PieceToken("s", 1, 2, 0, 2));
		pieceNames.put("t", new PieceToken("t", 1, 2, 2, 2));
		pieceNames.put("u", new PieceToken("u", 0, 2, 0, 0));
		pieceNames.put("v", new PieceToken("v", 0, 2, 2, 0));
		pieceNames.put("w", new PieceToken("w", 0, 2, 0, 2));
		pieceNames.put("x", new PieceToken("x", 0, 2, 2, 2));

		pieceNames.put("A", new PieceToken("A", 1, 2, 1, 1));
		pieceNames.put("B", new PieceToken("B", 0, 0, 0, 0));
		pieceNames.put("C", new PieceToken("C", 1, 2, 1, 2));
		pieceNames.put("D", new PieceToken("D", 1, 0, 1, 2));
		pieceNames.put("E", new PieceToken("E", 1, 0, 1, 0));
		pieceNames.put("F", new PieceToken("F", 1, 0, 1, 1));
		pieceNames.put("G", new PieceToken("G", 1, 2, 2, 1));
		pieceNames.put("H", new PieceToken("H", 1, 2, 0, 1));
		pieceNames.put("I", new PieceToken("I", 1, 0, 2, 1));
		pieceNames.put("J", new PieceToken("J", 1, 0, 0, 1));
		pieceNames.put("K", new PieceToken("K", 2, 2, 2, 2));
		pieceNames.put("L", new PieceToken("L", 1, 1, 1, 1));
		pieceNames.put("M", new PieceToken("M", 1, 0, 0, 0));
		pieceNames.put("N", new PieceToken("N", 1, 0, 0, 2));
		pieceNames.put("O", new PieceToken("O", 1, 2, 0, 0));
		pieceNames.put("P", new PieceToken("P", 1, 0, 2, 0));
		pieceNames.put("Q", new PieceToken("Q", 1, 0, 2, 2));
		pieceNames.put("R", new PieceToken("R", 1, 2, 2, 0));
		pieceNames.put("S", new PieceToken("S", 1, 2, 0, 2));
		pieceNames.put("T", new PieceToken("T", 1, 2, 2, 2));
		pieceNames.put("U", new PieceToken("U", 0, 2, 0, 0));
		pieceNames.put("V", new PieceToken("V", 0, 2, 2, 0));
		pieceNames.put("W", new PieceToken("W", 0, 2, 0, 2));
		pieceNames.put("X", new PieceToken("X", 0, 2, 2, 2));
	}
}
