package controller;

import persistence.FilePersistence;
import view.MainWindow;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlanningController controller =
                    new PlanningController(new FilePersistence("planning.dat"));

            new MainWindow(controller).setVisible(true);
        });
    }
}
