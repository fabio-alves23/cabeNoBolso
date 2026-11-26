package model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimulationResult {

    private final boolean hasSolution;
    private final List<Expense> keptExpenses;
    private final List<Expense> removedExpenses;
    private final Map<Integer, Long> totals;
    private final Map<Integer, Long> excesses;
    private final int totalPriority;

    private SimulationResult(boolean hasSolution,
                             List<Expense> keptExpenses,
                             List<Expense> removedExpenses,
                             Map<Integer, Long> totals,
                             Map<Integer, Long> excesses,
                             int totalPriority) {

        this.hasSolution = hasSolution;
        this.keptExpenses = keptExpenses;
        this.removedExpenses = removedExpenses;
        this.totals = totals;
        this.excesses = excesses;
        this.totalPriority = totalPriority;
    }

    public static SimulationResult withSolution(
            List<Expense> keptExpenses,
            List<Expense> removedExpenses,
            Map<Integer, Long> totals,
            int totalPriority) {

        return new SimulationResult(
                true,
                keptExpenses,
                removedExpenses,
                totals,
                Collections.emptyMap(),
                totalPriority
        );
    }

    public static SimulationResult withoutSolution(Map<Integer, Long> excesses) {
        return new SimulationResult(
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyMap(),
                excesses,
                0
        );
    }

    public boolean hasSolution() {
        return hasSolution;
    }

    public List<Expense> getKeptExpenses() {
        return Collections.unmodifiableList(keptExpenses);
    }

    public List<Expense> getRemovedExpenses() {
        return Collections.unmodifiableList(removedExpenses);
    }

    public Map<Integer, Long> getTotals() {
        return Collections.unmodifiableMap(totals);
    }

    public Map<Integer, Long> getExcesses() {
        return Collections.unmodifiableMap(excesses);
    }

    public int getTotalPriority() {
        return totalPriority;
    }

    public long getTotalCost() {
        long total = 0;

        for (long value : totals.values()) {
            total += value;
        }

        return total;
    }
}
