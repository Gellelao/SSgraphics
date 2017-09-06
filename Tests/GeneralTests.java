package Tests;

import static org.junit.Assert.assertEquals;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.junit.Test;

import src.gui.BoardPanelController;
import src.gui.View;
import src.model.Board;
import src.model.Token;

/**
 * Most tests here are like this: they show the program briefly, perform an action, then close.
 * It may be helpful to run some of the individually to see what is happening better
 * I had some trouble playing animations using the 'showThenDestroy()' method, so most of the animations are cut off at the end.
 * 
 * @author Deacon
 *
 */
public class GeneralTests {
	/**
	 * Method from Marco's TestableWheatRun code, shows the provided view for a few seconds, then disposes of it
	 * 
	 * @param v - the view to show
	 * @throws InterruptedException
	 */
	private void showThenDestroy(View v) throws InterruptedException {
		SwingUtilities.invokeLater(()->{ new Timer(2000,e->v.dispose()).start();});
		Thread.sleep(1000);
	}
	
	/**
	 * Shows the user the window, then closes it. Very basic test to make sure the JPanel layout and buttons sit right
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMenuLooksCorrect() throws InterruptedException {
		View v= new View(new Board());
		showThenDestroy(v);
	}
	
	@Test
	public void testGameLooksCorrect() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		showThenDestroy(v);
	}
	
	/**
	 * Creates a Token then pushes it off the left side of the board
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPiecesDieCorrectly() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		b.spawnToken(new Token("A", 1, 2, 1, 1));
		
		v.getSuper().pass();
		BoardPanelController boardControl = (BoardPanelController) v.boardPanel.getController();
		
		showThenDestroy(v);
		v.boardPanel.getController().processClick(307, 200);
		boardControl.processKey('a');
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		boardControl.processKey('a');
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		boardControl.processKey('a');
		// Check that A is off the board (getTokenRow returns -1 when it can't find the Token)
		assertEquals(-1, b.getTokenRow("A"));
	}
	
	/**
	 * Creates two tokens and uses one to push the other to the left
	 * In addition to window shown, this test uses an assert to make sure Tokens are where they should be
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPiecesPushOthers() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		Token A = new Token("A", 1, 2, 1, 1);
		Token B = new Token("B", 1, 2, 1, 1);
		b.spawnToken(A);
		
		Token[][] board = b.getBoard();
		
		v.getSuper().pass();
		BoardPanelController boardControl = (BoardPanelController) v.boardPanel.getController();
		
		showThenDestroy(v);
		v.boardPanel.getController().processClick(307, 200);
		boardControl.processKey('a');
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		v.getSuper().pass();
		b.spawnToken(B);
		v.boardPanel.getController().processClick(307, 200);
		boardControl.processKey('a');
		// Check that piece A has been pushed by piece B
		assertEquals(A, board[0][2]);
	}
	
	/**
	 * Creates a token, move it, then undoes that move. This is the only test that does not bring up the game window,
	 * it simply uses an assert
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testUndo() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		Token A = new Token("A", 1, 2, 1, 1);
		b.spawnToken(A);
		
		Token[][] board = b.getBoard();
		
		v.getSuper().pass();
		BoardPanelController boardControl = (BoardPanelController) v.boardPanel.getController();
		
		v.boardPanel.getController().processClick(307, 200);
		boardControl.processKey('a');
		// Undo should put it back to the spawn location: [2,2]
		v.getSuper().undo();
		// Need to check the name instead of the object be cause Undo() uses cloning
		assertEquals("A", board[2][2].toString());
	}

	//==================
	// Player One Tests:
	//==================
	
	/**
	 * Tests the transition from token choosing to rotation choosing for player 1
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPlayerPanelSwitchesP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		showThenDestroy(v);
		v.playerPanel1.getController().processClick(194, 352);
	}
	
	/**
	 * THIS TEST MAKES NOISE (when it runs as intended)
	 * 
	 * Tests that a beep is made when a user tries to click their opponents Tokens during the creation phase
	 * This is only one example of when the beep is played
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testErrorNoiseWhenNotYourTurnP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		showThenDestroy(v);
		v.playerPanel2.getController().processClick(194, 352);
	}
	
	/**
	 * Tests the animation for creating a Token for player 1
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testTokenCreationP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		v.switchToCard(v.p1, "Card with the four rotations of a token");
		v.setSelectionPanelToken(new Token("A", 1, 2, 1, 1), "1");
		showThenDestroy(v);
		v.selectionPanel1.getController().processClick(173, 504);
	}
	
	/**
	 * Tests that player 1 can move their tokens with the mouse
	 * (A bit hard to see because the end of the animation doesn't show)
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMouseMovementP1() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		b.spawnToken(new Token("A", 1, 2, 1, 1));
		
		showThenDestroy(v);
		v.getSuper().pass();
		v.boardPanel.getController().processClick(307, 200);
		v.boardPanel.getController().processClick(334, 200);
	}
	
	/**
	 * Tests that player 1 can move their tokens with WASD
	 * (A bit hard to see because the end of the animation doesn't show)
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testKeysMovementP1() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		b.spawnToken(new Token("A", 1, 2, 1, 1));
		
		showThenDestroy(v);
		v.getSuper().pass();
		v.boardPanel.getController().processClick(307, 200);
		BoardPanelController boardControl = (BoardPanelController) v.boardPanel.getController();
		boardControl.processKey('d');
	}
	
	//==================
	// Player Two Tests:
	//==================
	// The same as the player 1 tests above, but for the green player (player 2)
	// These are achieved by passing twice, which skips the yellow player's turn.
	@Test
	public void testPlayerPanelSwitchesP2() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		showThenDestroy(v);
		v.playerPanel2.getController().processClick(194, 352);
	}
	

	// THIS TEST MAKES NOISE (when it runs as intended)
	@Test
	public void testErrorNoiseWhenNotYourTurnP2() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		v.getSuper().pass();
		v.getSuper().pass();
		
		showThenDestroy(v);
		v.playerPanel2.getController().processClick(194, 352);
	}
	
	@Test
	public void testTokenCreationP2() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		v.getSuper().pass();
		v.getSuper().pass();
		v.switchToCard(v.p2, "Card with the four rotations of a token");
		v.setSelectionPanelToken(new Token("a", 1, 2, 1, 1), "2");
		
		showThenDestroy(v);
		v.selectionPanel2.getController().processClick(173, 504);
	}
	
	@Test
	public void testMouseMovementP2() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		v.getSuper().pass();
		v.getSuper().pass();
		
		b.spawnToken(new Token("a", 1, 2, 1, 1));
		
		showThenDestroy(v);
		v.getSuper().pass();
		v.boardPanel.getController().processClick(639, 536);
		v.boardPanel.getController().processClick(668, 539);
	}
	
	@Test
	public void testKeysMovementP2() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		v.getSuper().pass();
		v.getSuper().pass();
		
		b.spawnToken(new Token("a", 1, 2, 1, 1));
		
		showThenDestroy(v);
		v.getSuper().pass();
		v.boardPanel.getController().processClick(639, 536);
		BoardPanelController boardControl = (BoardPanelController) v.boardPanel.getController();
		boardControl.processKey('d');
	}
}
