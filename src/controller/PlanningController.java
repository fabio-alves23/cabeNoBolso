package controller;

import model.*;
import persistence.Persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlanningController {

    private TripPlan plan = new TripPlan();
    private final Persistence persistence;
    private final Simulator simulator = new Simulator();

    public PlanningController(Persistence persistence) {
        this.persistence = persistence;
    }

    // PARTICIPANTES

    public void addParticipant(String name, String budget)
            throws PlanningException {

        int code = 1;

        for (Participant p : plan.getParticipants()) {
            code = Math.max(code, p.getCode() + 1);
        }

        plan.addParticipant(code, name, readMoney(budget));
    }

    public void updateParticipant(String code, String name, String budget)
            throws PlanningException {
        plan.updateParticipant(readCode(code), name, readMoney(budget));
    }

    public void removeParticipant(String code) throws PlanningException {
        plan.removeParticipant(readCode(code));
    }

    public Collection<Participant> getParticipants() {
        return plan.getParticipants();
    }

    // DESPESAS

    public void addExpense(String description, String amount, boolean perPerson,
                           List<Integer> codes, String priority)
            throws PlanningException {
        plan.addExpense(createExpense(description, amount, perPerson, codes, priority));
    }

    public void updateExpense(int index, String description, String amount,
                              boolean perPerson, List<Integer> codes, String priority)
            throws PlanningException {
        plan.updateExpense(index,
                createExpense(description, amount, perPerson, codes, priority));
    }

    public void removeExpense(int index) throws PlanningException {
        plan.removeExpense(index);
    }

    public List<Expense> getExpenses() {
        return plan.getExpenses();
    }

    public String getExpenseType(Expense expense) {
        return expense instanceof PerPersonExpense ? "Por pessoa" : "Fixa";
    }

    public String getExpenseClassification(Expense expense) {
        return expense.isEssential() ? "Essencial"
                : "Opcional (prioridade " + expense.getPriority() + ")";
    }

    private Expense createExpense(String description, String amount,
                                  boolean perPerson, List<Integer> codes,
                                  String priority) throws PlanningException {

        List<Participant> participants = new ArrayList<>();

        if (codes != null) {
            for (int code : codes) {
                participants.add(plan.findParticipant(code));
            }
        }

        Expense expense = perPerson
                ? new PerPersonExpense(description, readMoney(amount), participants)
                : new FixedExpense(description, readMoney(amount), participants);

        expense.setPriority(readPriority(priority));
        return expense;
    }

    // RESUMO

    public String generateSummary() {
        StringBuilder result = new StringBuilder("DESPESAS\n");

        if (plan.getExpenses().isEmpty()) {
            result.append("(nenhuma despesa)\n");
        }

        for (Expense expense : plan.getExpenses()) {
            result.append("- ").append(expense.getDescription())
                    .append(" (").append(getExpenseType(expense)).append("): ")
                    .append(formatMoney(expense.getAmount())).append("\n");
        }

        result.append("\nCUSTO TOTAL: ")
                .append(formatMoney(plan.calculateTotalCost()))
                .append("\n\nPARTICIPANTES\n");

        try {
            for (Participant p : plan.getParticipants()) {
                long total = plan.calculateIndividualTotal(p.getCode());
                long extra = plan.calculateOverBudget(p.getCode());

                result.append(p.getName())
                        .append(": ").append(formatMoney(total))
                        .append(" | orçamento ").append(formatMoney(p.getBudget()))
                        .append(" | ")
                        .append(extra > 0 ? "EXCEDE em " + formatMoney(extra)
                                : "dentro do orçamento")
                        .append("\n");
            }
        } catch (PlanningException e) {
            throw new IllegalStateException(e);
        }

        return result.toString();
    }

    // SIMULADOR

    public String simulate() throws PlanningException {
        SimulationResult simulation = simulator.simulate(plan);
        StringBuilder result = new StringBuilder("RESULTADO DA SIMULAÇÃO\n");

        result.append("(o planejamento original não foi alterado)\n\n");

        if (!simulation.hasSolution()) {
            result.append("SEM SOLUÇÃO: as despesas essenciais já passam do orçamento.\n");

            for (Participant p : plan.getParticipants()) {
                Long extra = simulation.getExcesses().get(p.getCode());

                if (extra != null) {
                    result.append(p.getName()).append(" excede em ")
                            .append(formatMoney(extra)).append("\n");
                }
            }

            return result.toString();
        }

        result.append("DESPESAS MANTIDAS\n");

        for (Expense e : simulation.getKeptExpenses()) {
            result.append("- ").append(e.getDescription())
                    .append(" [").append(getExpenseClassification(e)).append("]\n");
        }

        result.append("\nDESPESAS RETIRADAS\n");

        for (Expense e : simulation.getRemovedExpenses()) {
            result.append("- ").append(e.getDescription())
                    .append(" [").append(getExpenseClassification(e)).append("]\n");
        }

        result.append("\nSoma das prioridades mantidas: ")
                .append(simulation.getTotalPriority())
                .append("\n\nNOVOS TOTAIS\n");

        for (Participant p : plan.getParticipants()) {
            result.append(p.getName()).append(": ")
                    .append(formatMoney(simulation.getTotals().get(p.getCode())))
                    .append(" | orçamento ").append(formatMoney(p.getBudget()))
                    .append("\n");
        }

        return result.append("\nNOVO CUSTO TOTAL: ")
                .append(formatMoney(simulation.getTotalCost())).toString();
    }

    // SALVAR, CARREGAR E NOVO PLANEJAMENTO

    public void save() throws PlanningException {
        persistence.save(plan);
    }

    public void load() throws PlanningException {
        plan = persistence.load();
    }

    public void newPlan() {
        plan = new TripPlan();
    }

    // CONVERSÃO DE DADOS

    private int readCode(String text) throws PlanningException {
        try {
            if (text == null || text.trim().isEmpty()) {
                throw new NumberFormatException();
            }

            return Integer.parseInt(text.trim());

        } catch (NumberFormatException e) {
            throw new PlanningException("Informe um código inteiro válido.");
        }
    }

    private int readPriority(String text) throws PlanningException {
        if (text == null || text.trim().isEmpty()) return 0;

        try {
            int priority = Integer.parseInt(text.trim());

            if (priority < 0) {
                throw new PlanningException("A prioridade não pode ser negativa.");
            }

            return priority;

        } catch (NumberFormatException e) {
            throw new PlanningException("A prioridade deve ser um número inteiro.");
        }
    }

    private long readMoney(String text) throws PlanningException {
        if (text == null || text.trim().isEmpty()) {
            throw new PlanningException("Preencha o campo de valor.");
        }

        String value = text.trim();

        if (value.contains(",")) {
            value = value.replace(".", "").replace(",", ".");
        }

        try {
            return new BigDecimal(value)
                    .setScale(2, RoundingMode.UNNECESSARY)
                    .movePointRight(2).longValueExact();

        } catch (NumberFormatException e) {
            throw new PlanningException("Valor inválido: " + text);

        } catch (ArithmeticException e) {
            throw new PlanningException("Use no máximo 2 casas decimais.");
        }
    }

    public static String formatNumber(long cents) {
        return String.format("%d,%02d", cents / 100, cents % 100);
    }

    public static String formatMoney(long cents) {
        return "R$ " + formatNumber(cents);
    }
}
