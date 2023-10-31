/**
 * Author: Declan ONUNKWO
 * College: SUNY Oswego
 * CSC 365 Project 2
 * Fall 2023
 */

import javax.swing.*;

public class MainClass {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainClass::runGui);
    }

    private static void runGui(){
        SimilarityAlgorithm gui = new SimilarityAlgorithm();
        JFrame frame = new JFrame();
        frame.setContentPane(gui.getPanel());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
