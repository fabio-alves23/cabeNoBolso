package modelo;

import java.util.Arrays;
import java.util.List;

/**
 * Teste do simulador com o caso de referência da proposta.
 * Transporte e almoço são essenciais; atividade recreativa é opcional (prioridade 3);
 * sobremesa é opcional (prioridade 1).
 * Para rodar: botão direito no arquivo > Run 'TesteSimulador.main()'.
 */
public class TesteSimulador {

    public static void main(String[] args) throws PlanejamentoException {
        Planejamento plano = montarCasoPrincipal();
        Simulador simulador = new Simulador();

        // ===== CASO DE REFERÊNCIA =====
        System.out.println("=== SIMULAÇÃO (esperado: retira a Sobremesa, mantém a Atividade) ===");
        imprimir(plano, simulador.simular(plano));

        // ===== O ORIGINAL NÃO PODE MUDAR =====
        System.out.println("\n=== PLANEJAMENTO ORIGINAL PRESERVADO (esperado: 4 despesas, R$ 210,00) ===");
        System.out.println("Despesas: " + plano.getDespesas().size()
                + " | Custo total: " + reais(plano.calcularCustoTotal()));

        // ===== CASO SEM SOLUÇÃO =====
        plano.alterarParticipante(1, "Ana", 4000); // orçamento da Ana = R$ 40,00
        System.out.println("\n=== ANA COM R$ 40,00 (esperado: sem solução, excesso de R$ 10,00) ===");
        imprimir(plano, simulador.simular(plano));
    }

    /** Monta o caso principal com as prioridades do simulador. */
    private static Planejamento montarCasoPrincipal() throws PlanejamentoException {
        Planejamento plano = new Planejamento();
        plano.cadastrarParticipante(1, "Ana", 7000);
        plano.cadastrarParticipante(2, "Bruno", 6000);
        plano.cadastrarParticipante(3, "Carla", 7000);
        Participante ana = plano.buscarParticipante(1);
        Participante bruno = plano.buscarParticipante(2);
        Participante carla = plano.buscarParticipante(3);
        List<Participante> todos = Arrays.asList(ana, bruno, carla);

        plano.incluirDespesa(new DespesaFixa("Transporte", 9000, todos));        // essencial
        plano.incluirDespesa(new DespesaPorPessoa("Almoço", 2000, todos));       // essencial
        Despesa atividade = new DespesaFixa("Atividade recreativa", 3000, todos);
        atividade.setPrioridade(3);                                              // opcional, prioridade 3
        plano.incluirDespesa(atividade);
        Despesa sobremesa = new DespesaPorPessoa("Sobremesa", 1500, Arrays.asList(ana, carla));
        sobremesa.setPrioridade(1);                                              // opcional, prioridade 1
        plano.incluirDespesa(sobremesa);
        return plano;
    }

    /** Mostra o resultado da simulação. */
    private static void imprimir(Planejamento plano, ResultadoSimulacao r) throws PlanejamentoException {
        if (!r.temSolucao()) {
            System.out.println("SEM SOLUÇÃO: as despesas essenciais já passam do orçamento.");
            for (Participante p : plano.getParticipantes()) {
                Long excesso = r.getExcessos().get(p.getCodigo());
                if (excesso != null) {
                    System.out.println(p.getNome() + " excede em " + reais(excesso));
                }
            }
            return;
        }
        for (Despesa d : r.getMantidas()) {
            System.out.println("Mantida: " + d.getDescricao()
                    + (d.isEssencial() ? " (essencial)" : " (opcional, prioridade " + d.getPrioridade() + ")"));
        }
        for (Despesa d : r.getRetiradas()) {
            System.out.println("Retirada: " + d.getDescricao()
                    + " (opcional, prioridade " + d.getPrioridade() + ")");
        }
        System.out.println("Soma das prioridades mantidas: " + r.getSomaPrioridades());
        for (Participante p : plano.getParticipantes()) {
            System.out.println(p.getNome() + ": " + reais(r.getTotais().get(p.getCodigo()))
                    + " (orçamento " + reais(p.getOrcamento()) + ")");
        }
        System.out.println("Novo custo total: " + reais(r.getCustoTotal()));
    }

    /** Converte centavos em texto, ex.: 6000 vira "R$ 60,00". */
    private static String reais(long centavos) {
        return String.format("R$ %d,%02d", centavos / 100, centavos % 100);
    }
}