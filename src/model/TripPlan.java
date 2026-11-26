
package model;

import java.io.Serializable;
import java.util.*;

public class TripPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Integer, Participant> participants = new TreeMap<>();
    private final List<Expense> expenses = new ArrayList<>();

    // PARTICIPANTES

    public void addParticipant(int code, String name, long budget)
            throws PlanningException {

        if (participants.containsKey(code)) {
            throw new PlanningException("Este código já está cadastrado.");
        }

        participants.put(code, new Participant(code, name, budget));
    }

    public void updateParticipant(int code, String name, long budget)
            throws PlanningException {

        Participant participant = findParticipant(code);

        // Valida os dados antes de alterar o cadastro
        new Participant(code, name, budget);

        participant.setName(name);
        participant.setBudget(budget);
    }

    public void removeParticipant(int code) throws PlanningException {
        findParticipant(code);

        // Impede excluir participantes vinculados a despesas
        for (Expense expense : expenses) {
            for (Participant p : expense.getInvolvedParticipants()) {
                if (p.getCode() == code) {
                    throw new PlanningException(
                            "Este participante está vinculado a uma despesa.");
                }
            }
        }

        participants.remove(code);
    }

    public Participant findParticipant(int code) throws PlanningException {
        Participant participant = participants.get(code);

        if (participant == null) {
            throw new PlanningException("Participante não encontrado.");
        }

        return participant;
    }

    public Collection<Participant> getParticipants() {
        return Collections.unmodifiableCollection(participants.values());
    }

    // DESPESAS

    public void addExpense(Expense expense) throws PlanningException {
        validateExpense(expense);
        expenses.add(expense);
    }

    public void updateExpense(int index, Expense newExpense)
            throws PlanningException {

        validateIndex(index);
        validateExpense(newExpense);
        expenses.set(index, newExpense);
    }

    public void removeExpense(int index) throws PlanningException {
        validateIndex(index);
        expenses.remove(index);
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    // CÁLCULOS

    public long calculateIndividualTotal(int code) throws PlanningException {
        findParticipant(code);

        long total = 0;

        for (Expense expense : expenses) {
            total += expense.calculateShares().getOrDefault(code, 0L);
        }

        return total;
    }

    public long calculateTotalCost() {
        long total = 0;

        for (Expense expense : expenses) {
            for (long value : expense.calculateShares().values()) {
                total += value;
            }
        }

        return total;
    }

    public long calculateOverBudget(int code) throws PlanningException {
        Participant participant = findParticipant(code);
        long total = calculateIndividualTotal(code);

        return Math.max(0, total - participant.getBudget());
    }

    // VALIDAÇÕES

    private void validateExpense(Expense expense) throws PlanningException {
        if (expense == null) {
            throw new PlanningException("Despesa inválida.");
        }

        for (Participant p : expense.getInvolvedParticipants()) {
            // Confere se o participante pertence a este planejamento
            if (participants.get(p.getCode()) != p) {
                throw new PlanningException(
                        "O participante " + p.getName()
                                + " não está cadastrado neste planejamento.");
            }
        }
    }

    private void validateIndex(int index) throws PlanningException {
        if (index < 0 || index >= expenses.size()) {
            throw new PlanningException("Despesa não encontrada.");
        }
    }
}
