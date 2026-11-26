package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Simulador de alternativas.
 * Mantém todas as despesas essenciais e escolhe quais opcionais manter para obter
 * a MAIOR soma de prioridades, sem que ninguém ultrapasse o orçamento.
 *
 * Algoritmo (força bruta): com até 10 opcionais existem no máximo 2^10 = 1024
 * combinações. Cada combinação é testada e vence a de maior soma de prioridades.
 * Só lê o planejamento: o original nunca é alterado.
 */
public class Simulador {

    // Limite de despesas opcionais aceito pela proposta
    private static final int MAX_OPCIONAIS = 10;

    /** Simula as alternativas para o planejamento informado. */
    public ResultadoSimulacao simular(Planejamento planejamento) throws PlanejamentoException {
        List<Despesa> todas = planejamento.getDespesas();

        // 1) Separa essenciais e opcionais
        List<Despesa> opcionais = new ArrayList<>();
        List<Despesa> essenciais = new ArrayList<>();
        for (Despesa d : todas) {
            if (d.isEssencial()) {
                essenciais.add(d);
            } else {
                opcionais.add(d);
            }
        }
        if (opcionais.size() > MAX_OPCIONAIS) {
            throw new PlanejamentoException("O simulador aceita no máximo "
                    + MAX_OPCIONAIS + " despesas opcionais.");
        }

        // 2) Total de cada participante considerando só as essenciais
        Map<Integer, Long> base = new TreeMap<>();
        for (Participante p : planejamento.getParticipantes()) {
            base.put(p.getCodigo(), 0L);
        }
        for (Despesa d : essenciais) {
            somar(base, d.calcularRateio());
        }

        // 3) Se as essenciais já estouram o orçamento de alguém, não há solução
        Map<Integer, Long> excessos = new TreeMap<>();
        for (Participante p : planejamento.getParticipantes()) {
            long excesso = base.get(p.getCodigo()) - p.getOrcamento();
            if (excesso > 0) {
                excessos.put(p.getCodigo(), excesso);
            }
        }
        if (!excessos.isEmpty()) {
            return ResultadoSimulacao.semSolucao(excessos);
        }

        // 4) Calcula uma vez o rateio de cada opcional
        List<Map<Integer, Long>> rateios = new ArrayList<>();
        for (Despesa d : opcionais) {
            rateios.add(d.calcularRateio());
        }

        // 5) Testa todas as combinações. Cada bit da "máscara" diz se o opcional fica (1) ou sai (0).
        //    A contagem é decrescente: em caso de empate, vence a combinação que mantém mais despesas.
        int n = opcionais.size();
        int melhorMascara = 0;
        int melhorSoma = -1;
        Map<Integer, Long> melhorTotais = base;
        for (int mascara = (1 << n) - 1; mascara >= 0; mascara--) {
            Map<Integer, Long> totais = new TreeMap<>(base);
            int soma = 0;
            for (int i = 0; i < n; i++) {
                if ((mascara & (1 << i)) != 0) {
                    soma += opcionais.get(i).getPrioridade();
                    somar(totais, rateios.get(i));
                }
            }
            if (soma > melhorSoma && respeitaOrcamentos(planejamento, totais)) {
                melhorSoma = soma;
                melhorMascara = mascara;
                melhorTotais = totais;
            }
        }

        // 6) Monta o resultado mantendo a ordem original das despesas
        List<Despesa> mantidas = new ArrayList<>();
        List<Despesa> retiradas = new ArrayList<>();
        int indiceOpcional = 0;
        for (Despesa d : todas) {
            if (d.isEssencial()) {
                mantidas.add(d);
            } else {
                if ((melhorMascara & (1 << indiceOpcional)) != 0) {
                    mantidas.add(d);
                } else {
                    retiradas.add(d);
                }
                indiceOpcional++;
            }
        }
        return ResultadoSimulacao.comSolucao(mantidas, retiradas, melhorTotais, melhorSoma);
    }

    /** Soma o rateio de uma despesa aos totais acumulados. */
    private void somar(Map<Integer, Long> totais, Map<Integer, Long> rateio) {
        for (Map.Entry<Integer, Long> parte : rateio.entrySet()) {
            totais.merge(parte.getKey(), parte.getValue(), Long::sum);
        }
    }

    /** Confere se o total de cada participante cabe no seu orçamento (igual ao limite vale). */
    private boolean respeitaOrcamentos(Planejamento planejamento, Map<Integer, Long> totais) {
        for (Participante p : planejamento.getParticipantes()) {
            if (totais.get(p.getCodigo()) > p.getOrcamento()) {
                return false;
            }
        }
        return true;
    }
}