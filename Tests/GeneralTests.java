package Tests;

import static org.junit.Assert.assertEquals;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.junit.Test;

import src.gui.View;
import src.model.Board;
import src.model.Token;

public class GeneralTests {
	 void killIn3Sec(View v) throws InterruptedException {
		    SwingUtilities.invokeLater(()->{ new Timer(2000,e->v.dispose()).start();});
		    Thread.sleep(1000);
		    }
	
	@Test
	public void testMenuLooksCorrect() throws InterruptedException {
		View v= new View(new Board());
		killIn3Sec(v);
	}
	
	@Test
	public void testGameLooksCorrect() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		killIn3Sec(v);
	}
	
	@Test
	public void testPlayerPanelSwitchesP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		killIn3Sec(v);
		v.playerPanel1.getController().processClick(194, 352);
	}
	
	//======================
	// THIS TEST MAKES NOISE (when it runs as intended)
	//======================
	@Test
	public void testErrorNoiseWhenNotYourTurnP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		killIn3Sec(v);
		v.playerPanel2.getController().processClick(194, 352);
	}
	
	@Test
	public void testTokenCreationP1() throws InterruptedException {
		View v= new View(new Board());
		v.switchToCard(v.cards, "Card with the game contents");
		v.switchToCard(v.p1, "Card with the four rotations of a token");
		v.setSelectionPanelToken(new Token("A", 1, 2, 1, 1), "1");
		killIn3Sec(v);
		v.selectionPanel1.getController().processClick(173, 504);
	}
	
	@Test
	public void testMouseMovementP1() throws InterruptedException {
		Board b = new Board();
		View  v = new View(b);
		v.switchToCard(v.cards, "Card with the game contents");
		
		b.spawnToken(new Token("A", 1, 2, 1, 1));
		
		killIn3Sec(v);
		v.getSuper().pass();
		v.boardPanel.getController().processClick(307, 200);
		v.boardPanel.getController().processClick(334, 200);
	}
}
