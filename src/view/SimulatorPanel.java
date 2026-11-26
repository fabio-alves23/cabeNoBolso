package view;

import controller.PlanningController;

import javax.swing.*;
import java.awt.*;

public class SimulatorPanel extends JPanel {

    private final PlanningController controller;
    private final JTextArea resultArea = new JTextArea();

    public SimulatorPanel(PlanningController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));

        JButton simulateButton = new JButton("Simular despesas");

        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        simulateButton.addActionListener(e -> simulate());

        add(simulateButton, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    // Executa a simulação e mostra o resultado
    private void simulate() {
        try {
            resultArea.setText(controller.simulate());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro na simulação",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void updateSimulation() {
        resultArea.setText("");
    }
}
