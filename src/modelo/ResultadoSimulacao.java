package modelo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Guarda o resultado de uma simulação: o que foi mantido, o que foi retirado
 * e os novos totais. É só um objeto de consulta: o planejamento original não muda.
 */
public class ResultadoSimulacao {
    private final boolean temSolucao;               // false se nem as essenciais couberem
    private final List<Despesa> mantidas;           // essenciais + opcionais mantidas
    private final List<Despesa> retiradas;          // opcionais retiradas
    private final Map<Integer, Long> totais;        // novo total de cada participante (centavos)
    private final Map<Integer, Long> excessos;      // quanto as essenciais passam do orçamento
    private final int somaPrioridades;              // soma das prioridades mantidas

    private ResultadoSimulacao(boolean temSolucao, List<Despesa> mantidas, List<Despesa> retiradas,
                               Map<Integer, Long> totais, Map<Integer, Long> excessos,
                               int somaPrioridades) {
        this.temSolucao = temSolucao;
        this.mantidas = mantidas;
        this.retiradas = retiradas;
        this.totais = totais;
        this.excessos = excessos;
        this.somaPrioridades = somaPrioridades;
    }

    /** Cria um resultado com solução. */
    public static ResultadoSimulacao comSolucao(List<Despesa> mantidas, List<Despesa> retiradas,
                                                Map<Integer, Long> totais, int somaPrioridades) {
        return new ResultadoSimulacao(true, mantidas, retiradas, totais,
                Collections.<Integer, Long>emptyMap(), somaPrioridades);
    }

    /** Cria um resultado sem solução, guardando o excesso de cada participante. */
    public static ResultadoSimulacao semSolucao(Map<Integer, Long> excessos) {
        return new ResultadoSimulacao(false, Collections.<Despesa>emptyList(),
                Collections.<Despesa>emptyList(), Collections.<Integer, Long>emptyMap(),
                excessos, 0);
    }

    public boolean temSolucao() { return temSolucao; }

    public List<Despesa> getMantidas() { return Collections.unmodifiableList(mantidas); }

    public List<Despesa> getRetiradas() { return Collections.unmodifiableList(retiradas); }

    public Map<Integer, Long> getTotais() { return Collections.unmodifiableMap(totais); }

    public Map<Integer, Long> getExcessos() { return Collections.unmodifiableMap(excessos); }

    public int getSomaPrioridades() { return somaPrioridades; }

    /** Soma dos novos totais de todos os participantes (centavos). */
    public long getCustoTotal() {
        long soma = 0;
        for (long valor : totais.values()) {
            soma += valor;
        }
        return soma;
    }
}