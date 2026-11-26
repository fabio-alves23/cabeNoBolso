package model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PerPersonExpense extends Expense {
    private static final long serialVersionUID = 1L;

    public PerPersonExpense(String description, long amount, List<Participant> participants)
            throws PlanningException {
        super(description, amount, participants);
    }

    @Override
    public Map<Integer, Long> calculateShares() {
        Map<Integer, Long> shares = new LinkedHashMap<>();

        for (Participant participant : getInvolvedParticipants()) {
            shares.put(participant.getCode(), getAmount());
        }

        return shares;
    }
}
