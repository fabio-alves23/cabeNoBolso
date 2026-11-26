package view;

import controller.PlanningController;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {

    private final PlanningController controller;
    private final JTextArea summaryArea = new JTextArea();

    public SummaryPanel(PlanningController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));

        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        updateSummary();
    }

    // Atualiza as informações financeiras
    public void updateSummary() {
        try {
            summaryArea.setText(controller.generateSummary());
        } catch (Exception e) {
            summaryArea.setText("Erro ao gerar resumo: " + e.getMessage());
        }
    }
}
