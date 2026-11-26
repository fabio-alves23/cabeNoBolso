package controle;

import modelo.Despesa;
import modelo.DespesaFixa;
import modelo.DespesaPorPessoa;
import modelo.Participante;
import modelo.Planejamento;
import modelo.PlanejamentoException;
import modelo.ResultadoSimulacao;
import modelo.Simulador;
import persistencia.Persistencia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controlador (o "C" do MVC): intermediário entre a tela e o modelo.
 * Recebe os textos digitados na tela, converte e valida, manda o modelo agir
 * e devolve o resultado. Não tem nenhum código de interface gráfica.
 */
public class PlanejamentoControlador {

    // Planejamento em uso (só é trocado quando o carregamento dá certo)
    private Planejamento planejamento = new Planejamento();

    // Contrato de persistência: o controlador não sabe se é arquivo, banco etc.
    private final Persistencia persistencia;

    // Simulador de alternativas (objetivo extra)
    private final Simulador simulador = new Simulador();

    public PlanejamentoControlador(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    // ---------------------- PARTICIPANTES ----------------------

    public void cadastrarParticipante(String codigo, String nome, String orcamento)
            throws PlanejamentoException {
        planejamento.cadastrarParticipante(lerCodigo(codigo), nome, lerDinheiro(orcamento));
    }

    public void alterarParticipante(String codigo, String nome, String orcamento)
            throws PlanejamentoException {
        planejamento.alterarParticipante(lerCodigo(codigo), nome, lerDinheiro(orcamento));
    }

    public void excluirParticipante(String codigo) throws PlanejamentoException {
        planejamento.excluirParticipante(lerCodigo(codigo));
    }

    public Collection<Participante> listarParticipantes() {
        return planejamento.getParticipantes();
    }

    // ------------------------- DESPESAS -------------------------

    public void incluirDespesa(String descricao, String valor, boolean porPessoa,
                               List<Integer> codigos, String prioridade)
            throws PlanejamentoException {
        planejamento.incluirDespesa(criarDespesa(descricao, valor, porPessoa, codigos, prioridade));
    }

    public void editarDespesa(int indice, String descricao, String valor, boolean porPessoa,
                              List<Integer> codigos, String prioridade)
            throws PlanejamentoException {
        planejamento.editarDespesa(indice,
                criarDespesa(descricao, valor, porPessoa, codigos, prioridade));
    }

    public void excluirDespesa(int indice) throws PlanejamentoException {
        planejamento.excluirDespesa(indice);
    }

    public List<Despesa> listarDespesas() {
        return planejamento.getDespesas();
    }

    /** Devolve o nome do tipo da despesa, para mostrar na tela. */
    public String tipoDe(Despesa despesa) {
        return despesa instanceof DespesaPorPessoa ? "Por pessoa" : "Fixa";
    }

    /** Devolve a classificação da despesa (essencial ou opcional), para mostrar na tela. */
    public String classeDe(Despesa despesa) {
        return despesa.isEssencial()
                ? "Essencial"
                : "Opcional (prioridade " + despesa.getPrioridade() + ")";
    }

    /** Monta a despesa do tipo escolhido (polimorfismo: o retorno é do tipo abstrato). */
    private Despesa criarDespesa(String descricao, String valor, boolean porPessoa,
                                 List<Integer> codigos, String prioridade)
            throws PlanejamentoException {
        long centavos = lerDinheiro(valor);
        int prio = lerPrioridade(prioridade);
        List<Participante> envolvidos = new ArrayList<>();
        if (codigos != null) {
            for (int codigo : codigos) {
                envolvidos.add(planejamento.buscarParticipante(codigo));
            }
        }
        Despesa despesa;
        if (porPessoa) {
            despesa = new DespesaPorPessoa(descricao, centavos, envolvidos);
        } else {
            despesa = new DespesaFixa(descricao, centavos, envolvidos);
        }
        despesa.setPrioridade(prio);
        return despesa;
    }

    // -------------------------- RESUMO --------------------------

    /** Gera o texto do resumo: despesas, custo total e situação de cada participante. */
    public String gerarResumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("DESPESAS\n");
        if (planejamento.getDespesas().isEmpty()) {
            sb.append("(nenhuma despesa)\n");
        }
        for (Despesa d : planejamento.getDespesas()) {
            sb.append("- ").append(d.getDescricao())
                    .append(" (").append(tipoDe(d)).append("): ")
                    .append(formatarDinheiro(d.getValor())).append("\n");
        }
        sb.append("\nCUSTO TOTAL: ").append(formatarDinheiro(planejamento.calcularCustoTotal()));
        sb.append("\n\nPARTICIPANTES\n");
        try {
            for (Participante p : planejamento.getParticipantes()) {
                long total = planejamento.calcularTotalIndividual(p.getCodigo());
                long excedente = planejamento.calcularExcedente(p.getCodigo());
                String situacao = excedente > 0
                        ? "EXCEDE em " + formatarDinheiro(excedente)
                        : "dentro do orçamento";
                sb.append(p.getNome()).append(": ").append(formatarDinheiro(total))
                        .append(" | orçamento ").append(formatarDinheiro(p.getOrcamento()))
                        .append(" | ").append(situacao).append("\n");
            }
        } catch (PlanejamentoException e) {
            throw new IllegalStateException(e); // não deve acontecer: os participantes existem
        }
        return sb.toString();
    }

    // ------------------------- SIMULADOR -------------------------

    /**
     * Roda o simulador e devolve o texto do resultado.
     * O planejamento original não é alterado.
     */
    public String simular() throws PlanejamentoException {
        ResultadoSimulacao r = simulador.simular(planejamento);
        StringBuilder sb = new StringBuilder();
        sb.append("RESULTADO DA SIMULAÇÃO (o planejamento original não foi alterado)\n\n");

        if (!r.temSolucao()) {
            sb.append("SEM SOLUÇÃO: as despesas essenciais já passam do orçamento.\n\n");
            for (Participante p : planejamento.getParticipantes()) {
                Long excesso = r.getExcessos().get(p.getCodigo());
                if (excesso != null) {
                    sb.append(p.getNome()).append(" excede em ")
                            .append(formatarDinheiro(excesso)).append("\n");
                }
            }
            return sb.toString();
        }

        sb.append("DESPESAS MANTIDAS\n");
        if (r.getMantidas().isEmpty()) {
            sb.append("(nenhuma)\n");
        }
        for (Despesa d : r.getMantidas()) {
            sb.append("- ").append(d.getDescricao()).append(" [")
                    .append(classeDe(d)).append("]\n");
        }
        sb.append("\nDESPESAS RETIRADAS\n");
        if (r.getRetiradas().isEmpty()) {
            sb.append("(nenhuma)\n");
        }
        for (Despesa d : r.getRetiradas()) {
            sb.append("- ").append(d.getDescricao()).append(" [")
                    .append(classeDe(d)).append("]\n");
        }
        sb.append("\nSoma das prioridades mantidas: ").append(r.getSomaPrioridades());
        sb.append("\n\nNOVOS TOTAIS\n");
        for (Participante p : planejamento.getParticipantes()) {
            sb.append(p.getNome()).append(": ")
                    .append(formatarDinheiro(r.getTotais().get(p.getCodigo())))
                    .append(" | orçamento ").append(formatarDinheiro(p.getOrcamento()))
                    .append("\n");
        }
        sb.append("\nNOVO CUSTO TOTAL: ").append(formatarDinheiro(r.getCustoTotal()));
        return sb.toString();
    }

    // -------------------------- ARQUIVO --------------------------

    public void salvar() throws PlanejamentoException {
        persistencia.salvar(planejamento);
    }

    /** Só troca o planejamento em uso se o carregamento der certo (se falhar, lança erro). */
    public void carregar() throws PlanejamentoException {
        planejamento = persistencia.carregar();
    }

    /** Começa um planejamento vazio. */
    public void novo() {
        planejamento = new Planejamento();
    }

    // ---------------- CONVERSÃO E FORMATAÇÃO ----------------

    /** Converte o texto digitado em código inteiro. */
    private int lerCodigo(String texto) throws PlanejamentoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new PlanejamentoException("Preencha o código.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new PlanejamentoException("O código deve ser um número inteiro.");
        }
    }

    /** Converte a prioridade digitada: vazio = essencial (0); senão, inteiro positivo. */
    private int lerPrioridade(String texto) throws PlanejamentoException {
        if (texto == null || texto.trim().isEmpty()) {
            return 0;
        }
        try {
            int prioridade = Integer.parseInt(texto.trim());
            if (prioridade <= 0) {
                throw new PlanejamentoException(
                        "A prioridade deve ser um inteiro positivo (deixe vazio para despesa essencial).");
            }
            return prioridade;
        } catch (NumberFormatException e) {
            throw new PlanejamentoException("A prioridade deve ser um número inteiro.");
        }
    }

    /** Converte "70,00" (ou "70.00") em centavos (7000). Recusa vazio, letras e mais de 2 casas. */
    private long lerDinheiro(String texto) throws PlanejamentoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new PlanejamentoException("Preencha o campo de valor.");
        }
        String t = texto.trim();
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", "."); // formato brasileiro: 1.000,50
        }
        try {
            return new BigDecimal(t).movePointRight(2).longValueExact();
        } catch (NumberFormatException e) {
            throw new PlanejamentoException("Valor inválido: \"" + texto.trim() + "\".");
        } catch (ArithmeticException e) {
            throw new PlanejamentoException("Use no máximo 2 casas decimais.");
        }
    }

    /** 7500 vira "75,00". */
    public static String formatarNumero(long centavos) {
        return String.format("%d,%02d", centavos / 100, centavos % 100);
    }

    /** 7500 vira "R$ 75,00". */
    public static String formatarDinheiro(long centavos) {
        return "R$ " + formatarNumero(centavos);
    }
}