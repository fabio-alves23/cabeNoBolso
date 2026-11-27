package view;

import controller.PlanningController;
import model.PlanningException;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final PlanningController controller;

    // Armazena as referências dos painéis da interface
    private final ParticipantPanel participantPanel;
    private final ExpensePanel expensePanel;
    private final SummaryPanel summaryPanel;
    private final SimulatorPanel simulatorPanel;

    public MainWindow(PlanningController controller) {
        this.controller = controller;

        setTitle("Cabe no Bolso - Planejador de Despesas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cria os painéis de cada aba
        participantPanel = new ParticipantPanel(controller, this::updateAll);
        expensePanel = new ExpensePanel(controller, this::updateAll);
        summaryPanel = new SummaryPanel(controller);
        simulatorPanel = new SimulatorPanel(controller);

        // Organiza as abas
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Participantes", participantPanel);
        tabs.addTab("Despesas", expensePanel);
        tabs.addTab("Resumo", summaryPanel);
        tabs.addTab("Simulador", simulatorPanel);

        add(tabs, BorderLayout.CENTER);
        setJMenuBar(createMenu());
    }

    // Cria o menu de arquivo
    private JMenuBar createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Arquivo");

        JMenuItem save = new JMenuItem("Salvar");
        JMenuItem load = new JMenuItem("Carregar");
        JMenuItem newPlan = new JMenuItem("Novo planejamento");

        save.addActionListener(e -> execute(() -> controller.save()));

        load.addActionListener(e -> execute(() -> {
            controller.load();
            updateAll();
        }));

        newPlan.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja iniciar um novo planejamento?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                controller.newPlan();
                updateAll();
            }
        });

        menu.add(save);
        menu.add(load);
        menu.addSeparator();
        menu.add(newPlan);

        bar.add(menu);
        return bar;
    }

    // Atualiza todas as abas após uma alteração
    private void updateAll() {
        participantPanel.updateTable();
        expensePanel.updateParticipants();
        expensePanel.updateTable();
        summaryPanel.updateSummary();
        simulatorPanel.updateSimulation();
    }

    // Trata erros das operações
    private void execute(Action action) {
        try {
            action.run();

            JOptionPane.showMessageDialog(
                    this,
                    "Operação realizada com sucesso!"
            );

        } catch (PlanningException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Define uma ação que pode gerar erros do planejamento
    @FunctionalInterface
    private interface Action {
        void run() throws PlanningException;
    }
}