package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe abstrata que representa uma despesa do passeio.
 * Guarda o que é comum a todas: descrição, valor e participantes envolvidos.
 * Cada tipo de despesa (fixa ou por pessoa) define seu próprio rateio.
 */
public abstract class Despesa implements Serializable {
    // Identificador de versão da classe (exigido pela serialização)
    private static final long serialVersionUID = 1L;

    // Atributos privados (encapsulamento)
    private final String descricao;                 // ex.: "Transporte"
    private final long valor;                       // em centavos
    private final List<Participante> envolvidos;    // sempre ordenada pelo código

    // Prioridade da despesa: 0 = essencial (sempre mantida); maior que 0 = opcional
    private int prioridade;

    /** Construtor: valida os dados e guarda os participantes ordenados pelo código. */
    protected Despesa(String descricao, long valor, List<Participante> envolvidos)
            throws PlanejamentoException {
        validar(descricao, valor, envolvidos);
        this.descricao = descricao.trim();
        this.valor = valor;
        // Cópia ordenada: evita alterações externas e garante a ordem dos centavos
        List<Participante> copia = new ArrayList<>(envolvidos);
        copia.sort(Comparator.comparingInt(Participante::getCodigo));
        this.envolvidos = copia;
    }

    /** Retorna a descrição da despesa. */
    public String getDescricao() { return descricao; }

    /** Retorna o valor informado, em centavos. */
    public long getValor() { return valor; }

    /** Retorna os participantes envolvidos (lista somente para leitura). */
    public List<Participante> getEnvolvidos() {
        return Collections.unmodifiableList(envolvidos);
    }

    /** Retorna a prioridade (0 = essencial). */
    public int getPrioridade() { return prioridade; }

    /** Informa se a despesa é essencial (nunca é retirada pelo simulador). */
    public boolean isEssencial() { return prioridade == 0; }

    /** Define a prioridade: 0 para essencial, inteiro positivo para opcional. */
    public void setPrioridade(int prioridade) throws PlanejamentoException {
        if (prioridade < 0) {
            throw new PlanejamentoException("A prioridade deve ser um número inteiro positivo.");
        }
        this.prioridade = prioridade;
    }

    /**
     * Calcula quanto cada envolvido paga, em centavos.
     * A chave do mapa é o código do participante.
     * Método abstrato: cada subclasse implementa à sua maneira (polimorfismo).
     */
    public abstract Map<Integer, Long> calcularRateio();

    /** Regras: descrição preenchida, valor maior que zero, ao menos um participante sem repetição. */
    private static void validar(String descricao, long valor, List<Participante> envolvidos)
            throws PlanejamentoException {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new PlanejamentoException("A descrição da despesa não pode ficar vazia.");
        }
        if (valor <= 0) {
            throw new PlanejamentoException("O valor da despesa deve ser maior que zero.");
        }
        if (envolvidos == null || envolvidos.isEmpty()) {
            throw new PlanejamentoException("A despesa precisa de pelo menos um participante.");
        }
        Set<Integer> codigosVistos = new HashSet<>();
        for (Participante p : envolvidos) {
            if (p == null) {
                throw new PlanejamentoException("Participante inválido na despesa.");
            }
            if (!codigosVistos.add(p.getCodigo())) {
                throw new PlanejamentoException("Participante repetido na despesa: " + p.getNome() + ".");
            }
        }
    }
}