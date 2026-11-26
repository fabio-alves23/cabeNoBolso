package model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimulationResult {

    // Indica se a simulação encontrou uma solução
    private final boolean hasSolution;

    // Despesas mantidas e retiradas pela simulação
    private final List<Expense> keptExpenses;
    private final List<Expense> removedExpenses;

    // Gastos por participante e valores que ultrapassaram o orçamento
    private final Map<Integer, Long> totals;
    private final Map<Integer, Long> excesses;

    // Soma das prioridades das despesas mantidas
    private final long totalPriority;

    // Construtor privado para controlar a criação dos resultados
    private SimulationResult(boolean hasSolution,
                             List<Expense> keptExpenses,
                             List<Expense> removedExpenses,
                             Map<Integer, Long> totals,
                             Map<Integer, Long> excesses,
                             long totalPriority) {

        this.hasSolution = hasSolution;
        this.keptExpenses = keptExpenses;
        this.removedExpenses = removedExpenses;
        this.totals = totals;
        this.excesses = excesses;
        this.totalPriority = totalPriority;
    }

    // Cria um resultado quando existe uma solução válida
    public static SimulationResult withSolution(
            List<Expense> keptExpenses,
            List<Expense> removedExpenses,
            Map<Integer, Long> totals,
            long totalPriority) {

        return new SimulationResult(
                true,
                keptExpenses,
                removedExpenses,
                totals,
                Collections.emptyMap(),
                totalPriority
        );
    }

    // Cria um resultado quando não existe solução
    public static SimulationResult withoutSolution(
            Map<Integer, Long> excesses) {

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

    // Retorna listas e mapas sem permitir alterações externas
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

    public long getTotalPriority() {
        return totalPriority;
    }

    // Soma os gastos de todos os participantes
    public long getTotalCost() {
        long total = 0;

        for (long value : totals.values()) {
            total += value;
        }

        return total;
    }
}
