package view;

import controller.PlanningController;
import model.Participant;
import model.PlanningException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ParticipantPanel extends JPanel {

    private final PlanningController controller;
    private final Runnable onChange;

    private final JTextField nameField = new JTextField(15);
    private final JTextField budgetField = new JTextField(15);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Código", "Nome", "Limite de gastos"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public ParticipantPanel(PlanningController controller, Runnable onChange) {
        this.controller = controller;
        this.onChange = onChange;

        setLayout(new BorderLayout(5, 5));

        // Campos de cadastro
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Nome:"));
        form.add(nameField);
        form.add(new JLabel("Limite de gastos (R$):"));
        form.add(budgetField);

        // Botões
        JPanel buttons = new JPanel();

        JButton add = new JButton("Cadastrar");
        JButton update = new JButton("Alterar");
        JButton remove = new JButton("Excluir");
        JButton clear = new JButton("Limpar");

        add.addActionListener(e -> execute(() ->
                controller.addParticipant(
                        nameField.getText(),
                        budgetField.getText()
                )));

        update.addActionListener(e -> execute(() -> {
            int code = getSelectedCode();

            controller.updateParticipant(
                    String.valueOf(code),
                    nameField.getText(),
                    budgetField.getText()
            );
        }));

        remove.addActionListener(e -> {
            try {
                int code = getSelectedCode();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja excluir este participante?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.removeParticipant(String.valueOf(code));
                    updateTable();
                    clearFields();

                    if (onChange != null) {
                        onChange.run();
                    }
                }

            } catch (PlanningException ex) {
                showError(ex.getMessage());
            }
        });

        clear.addActionListener(e -> clearFields());

        buttons.add(add);
        buttons.add(update);
        buttons.add(remove);
        buttons.add(clear);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Preenche os campos ao selecionar um participante
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFields();
            }
        });

        updateTable();
    }

    // Recupera o código da pessoa selecionada na tabela
    private int getSelectedCode() throws PlanningException {
        int row = table.getSelectedRow();

        if (row < 0) {
            throw new PlanningException("Selecione um participante.");
        }

        return Integer.parseInt(
                tableModel.getValueAt(row, 0).toString());
    }

    private void fillFields() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            nameField.setText(
                    tableModel.getValueAt(row, 1).toString());

            budgetField.setText(
                    tableModel.getValueAt(row, 2).toString()
                            .replace("R$ ", ""));
        }
    }

    // Atualiza a tabela de participantes
    public void updateTable() {
        tableModel.setRowCount(0);

        for (Participant p : controller.getParticipants()) {
            tableModel.addRow(new Object[]{
                    p.getCode(),
                    p.getName(),
                    PlanningController.formatMoney(p.getBudget())
            });
        }
    }

    private void execute(Action action) {
        try {
            action.run();
            updateTable();
            clearFields();

            if (onChange != null) {
                onChange.run();
            }

        } catch (PlanningException | NumberFormatException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        budgetField.setText("");
        table.clearSelection();
    }

    @FunctionalInterface
    private interface Action {
        void run() throws PlanningException;
    }
}
