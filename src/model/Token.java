package src.model;
import java.util.ArrayList;

/**
 * Class controlling the pieces with swords and shields that are placed onto the board
 * An object of this class can represented by its "image", which is accessed with the getImage() method
 * Tokens have "names", which are single character identifiers - capital letters for yellow player
 *
 * @author Deacon
 *
 */
public class Token{
	private String name;
	// The defaultLayout should not be altered
	private ArrayList<Integer> defaultLayout;
	// layout changes when this piece is rotated
	private ArrayList<Integer> layout;

	public Token(String name, int N, int E, int S, int W){
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
	 * All the information needed to represent a token is a number for each side indicating whether that side
	 * has a sword, shield, or neither, and the name of the token
	 * 
	 * @return An array containing the required info to draw the token
	 */
	public String[] getImage() {
		String[] image = new String[5];
		image[0] = name;
		image[1] = Integer.toString(layout.get(0));
		image[2] = Integer.toString(layout.get(1));
		image[3] = Integer.toString(layout.get(2));
		image[4] = Integer.toString(layout.get(3));
		return image;
	}

	/**
	 * Moves the layout list around clockwise, so the Token rotates
	 */
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

	/**
	 * @return a new Token with the same properties as this one
	 */
	public Token clone() {
		return new Token(name, layout.get(0), layout.get(1), layout.get(2), layout.get(3));
	}
}
