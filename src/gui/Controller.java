package src.gui;

/**
 * Interface for the panel controllers. Each panel needs an corresponding implementation of this interface.
 * All controllers need to communicate with the SuperController, so they must all have a setSuper() method.
 * Likewise, all panel controllers have a field containing the panel they control, so they all have a getPanel()
 * method which returns that field
 * 
 * @author Deacon
 *
 */
public interface Controller {
	public AbstractGamePanel getPanel();
	public void setSuper(SuperController s);
	public void processClick(int x, int y);
}
