package model;

import java.util.*;

public class Simulator {

    private static final int MAX_OPTIONAL = 10;

    public SimulationResult simulate(TripPlan plan) throws PlanningException {
        List<Expense> allExpenses = plan.getExpenses();

        List<Expense> essential = new ArrayList<>();
        List<Expense> optional = new ArrayList<>();

        // Separa despesas essenciais das opcionais
        for (Expense expense : allExpenses) {
            if (expense.isEssential()) {
                essential.add(expense);
            } else {
                optional.add(expense);
            }
        }

        if (optional.size() > MAX_OPTIONAL) {
            throw new PlanningException(
                    "O simulador aceita no máximo " + MAX_OPTIONAL
                            + " despesas opcionais.");
        }

        // Calcula os gastos das despesas essenciais
        Map<Integer, Long> baseTotals = new TreeMap<>();

        for (Participant p : plan.getParticipants()) {
            baseTotals.put(p.getCode(), 0L);
        }

        for (Expense expense : essential) {
            addShares(baseTotals, expense.calculateShares());
        }

        // Verifica se as despesas essenciais já ultrapassam algum orçamento
        Map<Integer, Long> excesses = new TreeMap<>();

        for (Participant p : plan.getParticipants()) {
            long extra = baseTotals.get(p.getCode()) - p.getBudget();

            if (extra > 0) {
                excesses.put(p.getCode(), extra);
            }
        }

        if (!excesses.isEmpty()) {
            return SimulationResult.withoutSolution(excesses);
        }

        // Guarda o rateio de cada despesa opcional
        List<Map<Integer, Long>> sharesList = new ArrayList<>();

        for (Expense expense : optional) {
            sharesList.add(expense.calculateShares());
        }

        // Testa todas as combinações possíveis de despesas
        int quantity = optional.size();
        int bestMask = 0;
        int bestPriority = -1;
        int bestCount = -1;

        Map<Integer, Long> bestTotals = baseTotals;

        for (int mask = (1 << quantity) - 1; mask >= 0; mask--) {
            Map<Integer, Long> totals = new TreeMap<>(baseTotals);
            int prioritySum = 0;
            int keptCount = 0;

            for (int i = 0; i < quantity; i++) {
                if ((mask & (1 << i)) != 0) {
                    prioritySum += optional.get(i).getPriority();
                    keptCount++;
                    addShares(totals, sharesList.get(i));
                }
            }

            // Escolhe a combinação com maior prioridade sem exceder os orçamentos
            if (respectsBudgets(plan, totals)
                    && (prioritySum > bestPriority
                    || (prioritySum == bestPriority && keptCount > bestCount))) {

                bestPriority = prioritySum;
                bestCount = keptCount;
                bestMask = mask;
                bestTotals = totals;
            }
        }

        // Separa as despesas mantidas das retiradas
        List<Expense> kept = new ArrayList<>();
        List<Expense> removed = new ArrayList<>();
        int optionalIndex = 0;

        for (Expense expense : allExpenses) {
            if (expense.isEssential()) {
                kept.add(expense);
            } else {
                if ((bestMask & (1 << optionalIndex)) != 0) {
                    kept.add(expense);
                } else {
                    removed.add(expense);
                }
                optionalIndex++;
            }
        }

        return SimulationResult.withSolution(
                kept, removed, bestTotals, bestPriority);
    }

    // Soma os valores de cada rateio aos totais
    private void addShares(Map<Integer, Long> totals,
                           Map<Integer, Long> shares) {
        for (Map.Entry<Integer, Long> entry : shares.entrySet()) {
            totals.merge(entry.getKey(), entry.getValue(), Long::sum);
        }
    }

    // Verifica se todos os participantes continuam dentro do orçamento
    private boolean respectsBudgets(TripPlan plan,
                                    Map<Integer, Long> totals) {
        for (Participant p : plan.getParticipants()) {
            if (totals.get(p.getCode()) > p.getBudget()) {
                return false;
            }
        }

        return true;
    }
}
