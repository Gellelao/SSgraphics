package src.gui;

import javax.swing.SwingUtilities;

import src.model.Board;

public class Main {
  public static void main(String[] s) {
    SwingUtilities.invokeLater(()->new View(new Board()));
  }
}
