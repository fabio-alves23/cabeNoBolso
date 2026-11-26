package persistencia;

import modelo.DespesaFixa;
import modelo.DespesaPorPessoa;
import modelo.Participante;
import modelo.Planejamento;
import modelo.PlanejamentoException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Teste da persistência: salvar, recuperar sem duplicar registros
 * e tratar arquivo ausente ou inválido.
 * Para rodar: botão direito no arquivo > Run 'TesteArquivo.main()'.
 */
public class TesteArquivo {

    public static void main(String[] args) throws Exception {

        // Monta o caso principal da proposta
        Planejamento plano = new Planejamento();
        plano.cadastrarParticipante(1, "Ana", 7000);
        plano.cadastrarParticipante(2, "Bruno", 6000);
        plano.cadastrarParticipante(3, "Carla", 7000);
        Participante ana = plano.buscarParticipante(1);
        Participante bruno = plano.buscarParticipante(2);
        Participante carla = plano.buscarParticipante(3);
        List<Participante> todos = Arrays.asList(ana, bruno, carla);
        plano.incluirDespesa(new DespesaFixa("Transporte", 9000, todos));
        plano.incluirDespesa(new DespesaPorPessoa("Almoço", 2000, todos));
        plano.incluirDespesa(new DespesaFixa("Atividade recreativa", 3000, todos));
        plano.incluirDespesa(new DespesaPorPessoa("Sobremesa", 1500, Arrays.asList(ana, carla)));

        // ===== SALVAR E REINICIAR =====
        Persistencia arquivo = new PersistenciaArquivo("planejamento.dat");
        arquivo.salvar(plano);
        Planejamento recuperado = arquivo.carregar(); // simula o programa reaberto

        System.out.println("=== SALVAR E REINICIAR (esperado: 3 participantes, 4 despesas, R$ 210,00) ===");
        System.out.println("Participantes: " + recuperado.getParticipantes().size());
        System.out.println("Despesas: " + recuperado.getDespesas().size());
        System.out.println("Custo total: " + recuperado.calcularCustoTotal() + " centavos");
        for (Participante p : recuperado.getParticipantes()) {
            System.out.println(p.getNome() + ": " + recuperado.calcularTotalIndividual(p.getCodigo())
                    + " centavos | excedente: " + recuperado.calcularExcedente(p.getCodigo()));
        }

        // ===== ARQUIVO AUSENTE =====
        System.out.println("\n=== ARQUIVO AUSENTE (esperado: mensagem de erro) ===");
        try {
            new PersistenciaArquivo("nao_existe.dat").carregar();
        } catch (PlanejamentoException e) {
            System.out.println("Recusado -> " + e.getMessage());
        }

        // ===== ARQUIVO INVÁLIDO =====
        System.out.println("\n=== ARQUIVO INVÁLIDO (esperado: mensagem de erro) ===");
        Files.writeString(Paths.get("invalido.dat"), "isto não é um planejamento");
        try {
            new PersistenciaArquivo("invalido.dat").carregar();
        } catch (PlanejamentoException e) {
            System.out.println("Recusado -> " + e.getMessage());
        }

        // O planejamento em uso continua intacto após as falhas
        System.out.println("\nPlanejamento em uso após as falhas: "
                + plano.getDespesas().size() + " despesas (esperado: 4)");
    }
}