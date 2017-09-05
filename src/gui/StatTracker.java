package src.gui;

import src.model.Board;

/**
 * Keeps track of some of the statistics of the game, and is able to report these.
 * Needs a model field so be able to tell how many tokens have 'died'
 * 
 * @author Deacon
 *
 */
public class StatTracker {
	private Board myModel;

	private int moveCount;
	private int undoCount;
	private long startTime;

	public StatTracker(Board model) {
		myModel = model;
		startTime = System.currentTimeMillis();
	}

	/**
	 * Alters the 'moveCount' field by the 'shift' parameter, which is only ever 1 or -1
	 * The -1 is used when a move is undone
	 * 
	 * @param shift - the amount to change the moveCount by
	 */
	public void move(int shift) {moveCount += shift;}
	public void undo() {undoCount++;}
	
	/**
	 * @return the time in seconds since the game started
	 */
	public int getTime() {return (int)((System.currentTimeMillis() - startTime)/1000);}
	public int getDeaths() {return myModel.cemeterySize();}
	public int getMoves() {return moveCount;}
	public int getUndos() {return undoCount;}
}
