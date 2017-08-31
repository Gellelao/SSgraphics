package src.gui;

import src.model.Board;

public class StatTracker {
	private Board myModel;

	private int moveCount;
	private int undoCount;
	private long startTime;

	public StatTracker(Board model) {
		myModel = model;
		startTime = System.currentTimeMillis();
	}

	public void move(int shift) {moveCount += shift;}
	public void undo() {undoCount++;}

	public int getMoves() {return moveCount;}
	public int getUndos() {return undoCount;}
	public int getTime() {return (int)((System.currentTimeMillis() - startTime)/1000);}
	public int getDeaths() {return myModel.cemeterySize();}
}
