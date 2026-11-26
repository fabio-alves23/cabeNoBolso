package model;

import java.io.Serializable;
import java.util.*;

public abstract class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String description;
    private final long amount;
    private final List<Participant> involvedParticipants;

    private int priority = 0;

    protected Expense(String description, long amount,
                      List<Participant> participants) throws PlanningException {

        validateExpense(description, amount, participants);

        this.description = description.trim();
        this.amount = amount;

        // Ordena os participantes pelo código para manter o rateio organizado
        involvedParticipants = new ArrayList<>(participants);
        involvedParticipants.sort(
                Comparator.comparingInt(Participant::getCode));
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public List<Participant> getInvolvedParticipants() {
        return Collections.unmodifiableList(involvedParticipants);
    }

    public int getPriority() {
        return priority;
    }

    public boolean isEssential() {
        return priority == 0;
    }

    // Define a prioridade da despesa
    public void setPriority(int priority) throws PlanningException {
        if (priority < 0) {
            throw new PlanningException("A prioridade não pode ser negativa.");
        }

        this.priority = priority;
    }

    // Cada tipo de despesa calcula sua própria divisão
    public abstract Map<Integer, Long> calculateShares();

    // Verifica se os dados da despesa são válidos
    private void validateExpense(String description, long amount,
                                 List<Participant> participants)
            throws PlanningException {

        if (description == null || description.trim().isEmpty()) {
            throw new PlanningException("A descrição não pode estar vazia.");
        }

        if (amount <= 0) {
            throw new PlanningException("O valor deve ser maior que zero.");
        }

        if (participants == null || participants.isEmpty()) {
            throw new PlanningException("Selecione pelo menos um participante.");
        }

        Set<Integer> codes = new HashSet<>();

        for (Participant p : participants) {
            if (p == null || !codes.add(p.getCode())) {
                throw new PlanningException(
                        "Participante inválido ou selecionado mais de uma vez.");
            }
        }
    }
}
