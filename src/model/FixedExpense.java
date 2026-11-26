package model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FixedExpense extends Expense {

    private static final long serialVersionUID = 1L;

    public FixedExpense(String description, long amount,
                        List<Participant> participants) throws PlanningException {
        super(description, amount, participants);
    }

    @Override
    public Map<Integer, Long> calculateShares() {
        List<Participant> participants = getInvolvedParticipants();

        int quantity = participants.size();

        long share = getAmount() / quantity;
        long remainder = getAmount() % quantity;

        Map<Integer, Long> shares = new LinkedHashMap<>();

        // Distribui os centavos restantes pela ordem dos códigos
        for (int i = 0; i < quantity; i++) {
            long value = share + (i < remainder ? 1 : 0);
            shares.put(participants.get(i).getCode(), value);
        }

        return shares;
    }
}
