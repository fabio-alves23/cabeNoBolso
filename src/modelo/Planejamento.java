package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Representa o planejamento do passeio.
 * Guarda os participantes e as despesas e calcula os totais.
 * Toda operação inválida lança PlanejamentoException SEM alterar os dados.
 */
public class Planejamento implements Serializable {
    // Identificador de versão da classe (exigido pela serialização)
    private static final long serialVersionUID = 1L;

    // TreeMap: busca rápida pelo código e mantém os participantes ordenados por ele
    private final Map<Integer, Participante> participantes = new TreeMap<>();

    // ArrayList: mantém a ordem de cadastro e permite editar/excluir por posição
    private final List<Despesa> despesas = new ArrayList<>();

    // ---------------------- PARTICIPANTES ----------------------

    /** Cadastra um participante novo, recusando código duplicado. */
    public void cadastrarParticipante(int codigo, String nome, long orcamento)
            throws PlanejamentoException {
        if (participantes.containsKey(codigo)) {
            throw new PlanejamentoException("Já existe um participante com o código " + codigo + ".");
        }
        // O construtor valida os dados; se falhar, nada é guardado
        participantes.put(codigo, new Participante(codigo, nome, orcamento));
    }

    /** Altera nome e orçamento de um participante existente. */
    public void alterarParticipante(int codigo, String nome, long orcamento)
            throws PlanejamentoException {
        Participante p = buscarParticipante(codigo);
        // Cria um participante temporário só para validar os dois valores antes de mexer no original
        new Participante(codigo, nome, orcamento);
        p.setNome(nome);
        p.setOrcamento(orcamento);
    }

    /** Exclui um participante, desde que não tenha despesas vinculadas. */
    public void excluirParticipante(int codigo) throws PlanejamentoException {
        Participante p = buscarParticipante(codigo);
        for (Despesa d : despesas) {
            if (d.getEnvolvidos().contains(p)) {
                throw new PlanejamentoException("Não é possível excluir " + p.getNome()
                        + ": há despesas vinculadas a ele.");
            }
        }
        participantes.remove(codigo);
    }

    /** Busca um participante pelo código; lança erro se não existir. */
    public Participante buscarParticipante(int codigo) throws PlanejamentoException {
        Participante p = participantes.get(codigo);
        if (p == null) {
            throw new PlanejamentoException("Participante de código " + codigo + " não encontrado.");
        }
        return p;
    }

    /** Lista os participantes (somente leitura), ordenados pelo código. */
    public Collection<Participante> getParticipantes() {
        return Collections.unmodifiableCollection(participantes.values());
    }

    // ------------------------- DESPESAS -------------------------

    /** Inclui uma despesa nova. */
    public void incluirDespesa(Despesa despesa) throws PlanejamentoException {
        validarDespesa(despesa);
        despesas.add(despesa);
    }

    /** Substitui a despesa que está na posição indicada. */
    public void editarDespesa(int indice, Despesa nova) throws PlanejamentoException {
        validarIndice(indice);
        validarDespesa(nova);
        despesas.set(indice, nova);
    }

    /** Remove a despesa que está na posição indicada. */
    public void excluirDespesa(int indice) throws PlanejamentoException {
        validarIndice(indice);
        despesas.remove(indice);
    }

    /** Lista as despesas (somente leitura). */
    public List<Despesa> getDespesas() {
        return Collections.unmodifiableList(despesas);
    }

    // -------------------------- CÁLCULOS --------------------------
    // Os totais são sempre recalculados na hora, então ficam corretos após qualquer alteração.

    /** Soma o que o participante paga em todas as despesas (em centavos). */
    public long calcularTotalIndividual(int codigo) throws PlanejamentoException {
        buscarParticipante(codigo); // garante que o código existe
        long total = 0;
        for (Despesa d : despesas) {
            // Polimorfismo: a referência é do tipo abstrato, o cálculo é da subclasse
            total += d.calcularRateio().getOrDefault(codigo, 0L); // quem não participa paga zero
        }
        return total;
    }

    /** Soma tudo o que foi cobrado de todos os participantes (em centavos). */
    public long calcularCustoTotal() {
        long total = 0;
        for (Despesa d : despesas) {
            for (long parte : d.calcularRateio().values()) {
                total += parte;
            }
        }
        return total;
    }

    /** Retorna quanto o participante passou do orçamento (0 se estiver dentro do limite). */
    public long calcularExcedente(int codigo) throws PlanejamentoException {
        long excedente = calcularTotalIndividual(codigo) - buscarParticipante(codigo).getOrcamento();
        return Math.max(0, excedente); // total igual ao limite conta como dentro do orçamento
    }

    // ------------------------ VALIDAÇÕES ------------------------

    /** Confere se a despesa existe e se todos os envolvidos estão cadastrados neste planejamento. */
    private void validarDespesa(Despesa despesa) throws PlanejamentoException {
        if (despesa == null) {
            throw new PlanejamentoException("Despesa inválida.");
        }
        for (Participante p : despesa.getEnvolvidos()) {
            if (participantes.get(p.getCodigo()) != p) {
                throw new PlanejamentoException("O participante " + p.getNome()
                        + " não está cadastrado no planejamento.");
            }
        }
    }

    /** Confere se a posição da despesa existe. */
    private void validarIndice(int indice) throws PlanejamentoException {
        if (indice < 0 || indice >= despesas.size()) {
            throw new PlanejamentoException("Despesa não encontrada.");
        }
    }
}