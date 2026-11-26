package controller;

import persistence.FilePersistence;
import view.MainWindow;

import javax.swing.SwingUtilities;

public class Main {

    // Ponto de entrada do programa
    public static void main(String[] args) {

        // Inicia a interface na thread própria do Swing
        SwingUtilities.invokeLater(() -> {

            PlanningController controller =
                    new PlanningController(new FilePersistence("planning.dat"));

            new MainWindow(controller).setVisible(true);
        });
    }
}