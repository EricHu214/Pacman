/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Eric Hu
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                JFrame frame = new JFrame("Pacman: Alpha V1.0");
                frame.setSize(680,660);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.add(new PacmanGame());
                frame.setVisible(true);
            }
        });   
    }
}
