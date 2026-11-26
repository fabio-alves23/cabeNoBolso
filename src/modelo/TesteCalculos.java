package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Teste dos cálculos do modelo, sem interface gráfica.
 * Reproduz o caso de referência da proposta e alguns testes adicionais.
 * Para rodar: botão direito no arquivo > Run 'TesteCalculos.main()'.
 */
public class TesteCalculos {

    public static void main(String[] args) throws PlanejamentoException {

        // ========== CASO PRINCIPAL (Ana, Bruno e Carla) ==========
        Planejamento plano = new Planejamento();
        plano.cadastrarParticipante(1, "Ana", 7000);    // R$ 70,00
        plano.cadastrarParticipante(2, "Bruno", 6000);  // R$ 60,00
        plano.cadastrarParticipante(3, "Carla", 7000);  // R$ 70,00

        Participante ana = plano.buscarParticipante(1);
        Participante bruno = plano.buscarParticipante(2);
        Participante carla = plano.buscarParticipante(3);
        List<Participante> todos = Arrays.asList(ana, bruno, carla);

        plano.incluirDespesa(new DespesaFixa("Transporte", 9000, todos));
        plano.incluirDespesa(new DespesaPorPessoa("Almoço", 2000, todos));
        plano.incluirDespesa(new DespesaFixa("Atividade recreativa", 3000, todos));
        plano.incluirDespesa(new DespesaPorPessoa("Sobremesa", 1500, Arrays.asList(ana, carla)));

        System.out.println("=== CASO PRINCIPAL (esperado: total R$ 210,00; Ana 75, Bruno 60, Carla 75) ===");
        imprimir(plano);

        // ========== TESTE: CENTAVOS NO RATEIO ==========
        Planejamento centavos = new Planejamento();
        centavos.cadastrarParticipante(1, "P1", 0);
        centavos.cadastrarParticipante(2, "P2", 0);
        centavos.cadastrarParticipante(3, "P3", 0);
        centavos.incluirDespesa(new DespesaFixa("Teste", 10000,
                new ArrayList<>(centavos.getParticipantes())));
        System.out.println("\n=== CENTAVOS (esperado: 33,34 / 33,33 / 33,33) ===");
        for (Participante p : centavos.getParticipantes()) {
            System.out.println(p.getNome() + ": " + reais(centavos.calcularTotalIndividual(p.getCodigo())));
        }

        // ========== TESTE: ENTRADAS INVÁLIDAS ==========
        System.out.println("\n=== ENTRADAS INVÁLIDAS (esperado: 3 recusas e nenhuma alteração) ===");
        int antes = plano.getDespesas().size();
        tentar("Sem participantes", () -> new DespesaFixa("X", 1000, new ArrayList<>()));
        tentar("Valor negativo", () -> new DespesaFixa("X", -500, todos));
        tentar("Campo vazio", () -> new DespesaFixa("", 1000, todos));
        System.out.println("Despesas antes: " + antes + " | depois: " + plano.getDespesas().size());

        // ========== TESTE: ALTERAÇÃO DE ORÇAMENTO ==========
        plano.alterarParticipante(1, "Ana", 8000); // Ana passa a ter R$ 80,00
        System.out.println("\n=== ORÇAMENTO DA ANA = R$ 80,00 (esperado: 75,00 e dentro do limite) ===");
        System.out.println("Ana: " + reais(plano.calcularTotalIndividual(1))
                + " | excedente: " + reais(plano.calcularExcedente(1)));
        plano.alterarParticipante(1, "Ana", 7000); // volta ao original

        // ========== TESTE: EXCLUSÃO DE DESPESA ==========
        plano.excluirDespesa(2); // posição 2 = Atividade recreativa
        System.out.println("\n=== SEM A ATIVIDADE (esperado: total R$ 180,00, média R$ 60,00 por pessoa) ===");
        imprimir(plano);
    }

    /** Mostra despesas, total, total individual e situação de cada participante. */
    private static void imprimir(Planejamento plano) throws PlanejamentoException {
        for (Despesa d : plano.getDespesas()) {
            System.out.println("Despesa: " + d.getDescricao() + " (" + reais(d.getValor()) + ")");
        }
        System.out.println("Custo total: " + reais(plano.calcularCustoTotal()));
        for (Participante p : plano.getParticipantes()) {
            long total = plano.calcularTotalIndividual(p.getCodigo());
            long excedente = plano.calcularExcedente(p.getCodigo());
            String situacao = excedente > 0 ? "EXCEDE em " + reais(excedente) : "dentro do orçamento";
            System.out.println(p.getNome() + ": " + reais(total)
                    + " (orçamento " + reais(p.getOrcamento()) + ") -> " + situacao);
        }
    }

    /** Executa uma ação que deve falhar e mostra a mensagem de erro. */
    private static void tentar(String nome, Acao acao) {
        try {
            acao.executar();
            System.out.println(nome + ": ERRO, deveria ter sido recusado!");
        } catch (PlanejamentoException e) {
            System.out.println(nome + ": recusado -> " + e.getMessage());
        }
    }

    /** Interface auxiliar para passar um trecho de código ao método tentar. */
    private interface Acao {
        void executar() throws PlanejamentoException;
    }

    /** Converte centavos em texto, ex.: 7500 vira "R$ 75,00". */
    private static String reais(long centavos) {
        return String.format("R$ %d,%02d", centavos / 100, centavos % 100);
    }
}