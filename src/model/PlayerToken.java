package src.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for storing information relating to the individual players
 * Stores the lists which determine whether or not a piece is playable, or changeable
 *
 * @author Deacon
 *
 */
public class PlayerToken implements Token{
	public String name;
	private List<String> available;
	private List<String> played;
	private ArrayList<String> changedPieces;

	private int spawnX;
	private int spawnY;

	public PlayerToken(String name){
		this.name = name;

		// available is a list of tokens that are available to be created
		available = new ArrayList<String>();
		// played is a list of tokens on the board - you can only move or rotate tokens in this list
		played = new ArrayList<String>();
		// changedPices contains the pieces that have been moved or rotated in a single turn
		// This list is cleared at the start of each turn
		changedPieces = new ArrayList<String>();

		// Change spawn coords and PieceToken type depending on which player this is
		if(name.equals("1")){
			spawnX = 2;
			spawnY = 2;

			// Fill the available tokens list with the name of every token
			// PlayerToken '1' gets capital letters:
			for (char ch = 'A'; ch <= 'X'; ch++){
				available.add(String.valueOf(ch));
			}
		}
		else{
			spawnX = 7;
			spawnY = 7;

			// Fill the available tokens list with the name of every token
			// PlayerToken '2' gets lowercase letters:
			for (char ch = 'a'; ch <= 'x'; ch++){
				available.add(String.valueOf(ch));
			}
		}
	}

	// TODO: update this if I ever revert the 'available' etc lists to hold tokens rather than strings
/*	public void listAvailable() {
		String[][] names = new String[5][5];
		String message = " ________________\n/Available Tokens\\";
		int cumulative = 0;
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if(cumulative < available.size())
					names[i][j] = available.get(cumulative);
				cumulative++;
			}
		}
		board.draw(names, message);
	}*/

	public boolean pieceIsChanged(String piece){
		return changedPieces.contains(piece);
	}

	public void changePiece(String piece){
		changedPieces.add(piece);
	}

	public void clearChangedPieces(){changedPieces.clear();}

	public String undoChanges(){
		if(!changedPieces.isEmpty()){
			return changedPieces.remove(changedPieces.size()-1);
		}
		return null;
	}

	public void undoCreate(){
		if(!played.isEmpty()){
			available.add(played.remove(played.size()-1));
		}
	}

	public boolean pieceIsAvailable(String piece){
		return available.contains(piece);
	}

	public void removeFromAvailable(String piece){
		available.remove(piece);
	}

	public boolean pieceIsPlayed(String piece){
		return played.contains(piece);
	}

	public void addToPlayed(String piece){
		played.add(piece);
	}

	public int getSpawnX(){return spawnX;}
	public int getSpawnY(){return spawnY;}

	public String toString() {
		return this.name;
	}

	@Override
	public String[] getImage() {
		String[] image = {"/   \\", "  " + name + "  ", "\\   /"};
		return image;
	}

	@Override
	public void rotate() {
		// Players cannot rotate
		return;
	}

	@Override
	public Token copy() {
		return new PlayerToken(name);
	}
}
