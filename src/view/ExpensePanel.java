package view;

import controller.PlanningController;
import model.Expense;
import model.Participant;
import model.PlanningException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpensePanel extends JPanel {

    private final PlanningController controller;
    private final Runnable onChange;

    private final JTextField descriptionField = new JTextField(15);
    private final JTextField amountField = new JTextField(10);
    private final JTextField priorityField = new JTextField("0", 5);

    private final JComboBox<String> typeCombo =
            new JComboBox<>(new String[]{"Fixa", "Por pessoa"});

    private final JPanel participantPanel = new JPanel();
    private final Map<Integer, JCheckBox> participantChecks =
            new LinkedHashMap<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Descrição", "Valor", "Tipo", "Classificação"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public ExpensePanel(PlanningController controller, Runnable onChange) {
        this.controller = controller;
        this.onChange = onChange;

        setLayout(new BorderLayout(5, 5));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        addField(form, c, 0, "Descrição:", descriptionField);
        addField(form, c, 1, "Valor (R$):", amountField);
        addField(form, c, 2, "Tipo:", typeCombo);
        addField(form, c, 3, "Prioridade (0 = essencial):", priorityField);

        participantPanel.setLayout(new BoxLayout(
                participantPanel, BoxLayout.Y_AXIS));

        JScrollPane participantScroll = new JScrollPane(participantPanel);
        participantScroll.setPreferredSize(new Dimension(200, 90));

        addField(form, c, 4, "Participantes:", participantScroll);

        JButton add = new JButton("Cadastrar");
        JButton update = new JButton("Alterar");
        JButton remove = new JButton("Excluir");
        JButton clear = new JButton("Limpar");

        add.addActionListener(e -> execute(() ->
                controller.addExpense(
                        descriptionField.getText(),
                        amountField.getText(),
                        typeCombo.getSelectedIndex() == 1,
                        getSelectedCodes(),
                        priorityField.getText()
                )));

        update.addActionListener(e -> execute(() ->
                controller.updateExpense(
                        table.getSelectedRow(),
                        descriptionField.getText(),
                        amountField.getText(),
                        typeCombo.getSelectedIndex() == 1,
                        getSelectedCodes(),
                        priorityField.getText()
                )));

        remove.addActionListener(e -> execute(() -> {
            int row = table.getSelectedRow();

            if (row < 0)
                throw new PlanningException("Selecione uma despesa.");

            int answer = JOptionPane.showConfirmDialog(
                    this, "Deseja excluir esta despesa?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION)
                controller.removeExpense(row);
        }));

        clear.addActionListener(e -> clearFields());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(update);
        buttons.add(remove);
        buttons.add(clear);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                fillFields();
        });

        updateParticipants();
        updateTable();
    }

    private void addField(JPanel panel, GridBagConstraints c,
                          int row, String label, Component field) {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(field, c);
    }

    private List<Integer> getSelectedCodes() {
        List<Integer> codes = new ArrayList<>();

        for (Map.Entry<Integer, JCheckBox> entry :
                participantChecks.entrySet()) {
            if (entry.getValue().isSelected())
                codes.add(entry.getKey());
        }

        return codes;
    }

    public void updateParticipants() {
        participantPanel.removeAll();
        participantChecks.clear();

        for (Participant p : controller.getParticipants()) {
            JCheckBox check = new JCheckBox(
                    p.getCode() + " - " + p.getName(), true);

            participantChecks.put(p.getCode(), check);
            participantPanel.add(check);
        }

        participantPanel.revalidate();
        participantPanel.repaint();
    }

    public void updateTable() {
        tableModel.setRowCount(0);

        for (Expense e : controller.getExpenses()) {
            tableModel.addRow(new Object[]{
                    e.getDescription(),
                    PlanningController.formatMoney(e.getAmount()),
                    controller.getExpenseType(e),
                    controller.getExpenseClassification(e)
            });
        }
    }

    private void fillFields() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Expense e = controller.getExpenses().get(row);

        descriptionField.setText(e.getDescription());
        amountField.setText(
                PlanningController.formatMoney(e.getAmount()).replace("R$ ", ""));

        typeCombo.setSelectedIndex(
                controller.getExpenseType(e).equals("Por pessoa") ? 1 : 0);

        priorityField.setText(String.valueOf(e.getPriority()));

        for (Map.Entry<Integer, JCheckBox> entry :
                participantChecks.entrySet()) {

            boolean selected = e.getInvolvedParticipants().stream()
                    .anyMatch(p -> p.getCode() == entry.getKey());

            entry.getValue().setSelected(selected);
        }
    }

    private void execute(Action action) {
        try {
            action.run();
            updateTable();
            clearFields();

            if (onChange != null)
                onChange.run();

        } catch (PlanningException | NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        descriptionField.setText("");
        amountField.setText("");
        priorityField.setText("0");
        typeCombo.setSelectedIndex(0);

        for (JCheckBox check : participantChecks.values())
            check.setSelected(true);

        table.clearSelection();
    }

    @FunctionalInterface
    private interface Action {
        void run() throws PlanningException;
    }
}
