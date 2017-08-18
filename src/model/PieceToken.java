package src.model;
import java.util.ArrayList;

/**
 * Class controlling the pieces with swords and shields that are placed onto the board
 * An object of this class can represented by its "image", which is accessed with the getImage() method
 * Pieces have "names", which are single character identifiers - capital letters for yellow player
 *
 * @author Deacon
 *
 */
public class PieceToken implements Token{
	private String name;
	// The defaultLayout should not be altered
	private ArrayList<Integer> defaultLayout;
	// layout changes when this piece is rotated
	private ArrayList<Integer> layout;

	public PieceToken(String name, int N, int E, int S, int W){
		defaultLayout = new ArrayList<Integer>();
		defaultLayout.add(N);
		defaultLayout.add(E);
		defaultLayout.add(S);
		defaultLayout.add(W);
		layout = new ArrayList<Integer>();
		layout.add(N);
		layout.add(E);
		layout.add(S);
		layout.add(W);
		this.name = name;
	}

	/**
	 * Gets the image of this piece, in the form of an array, with index 0 being the top row of the image,
	 * index 1 being the center row of the image, and index 2 being the bottom row of the image.

	 * Here "image" refers to the characters which represent the shape

	 * @return an array of strings, stacking these strings on top of each other results in the image of the token
	 */
	@Override
	public String[] getImage(){
		// First get the characters that will be drawn in each of the cardinal points:
		String N = getCorrespondingChar(layout.get(0), true);
		String E = getCorrespondingChar(layout.get(1), false);
		String S = getCorrespondingChar(layout.get(2), true);
		String W = getCorrespondingChar(layout.get(3), false);

		// Construct the image in three rows, stored in an array
		String[] image = {"  " + N + "  ", W + name + E, "  " + S + "  "};
		return image;
	}

	/**
	 * A helper method used by getImage()
	 *
	 * @param i - an integer:
	 * 			  0 = nothing
	 * 			  1 = sword
	 * 			  2 = shield
	 * @param vertical - a boolean indicating to return the vertical or horizontal version of the character
	 * @return a character representing a sword or shield, or a space.
	 */
	private String getCorrespondingChar(int i, boolean vertical){
		if(vertical){
			if(i == 0)     return " ";
			else if(i == 1)return "|"; // Vertical sword
			else           return "="; // Vertical shield
		}
		else{
			if(i == 0)     return "  ";
			else if(i == 1)return "--"; // Horizontal sword
			else           return "||"; // Horizontal shield
		}
	}

	@Override
	public void rotate() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(layout.get(3));
		for(int i = 0; i < 3; i++) {
			temp.add(layout.get(i));
		}
		layout = temp;
	}

	/**
	 * Sets this piece to its original orientation
	 */
	public void setToDefault() {
		layout.clear();
		for(int i = 0; i < 4; i++) {
			layout.add(defaultLayout.get(i));
		}
	}

	public String toString() {
		return this.name;
	}

	@Override
	public Token copy() {
		return new PieceToken(name, layout.get(0), layout.get(1), layout.get(2), layout.get(3));
	}
}
