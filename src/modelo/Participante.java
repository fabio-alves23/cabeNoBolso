package modelo;

import java.io.Serializable;

/**
 * Representa uma pessoa do passeio.
 * Guarda código único, nome e orçamento disponível.
 * Implementa Serializable para poder ser salva em arquivo.
 */
public class Participante implements Serializable {
    // Identificador de versão da classe (exigido pela serialização)
    private static final long serialVersionUID = 1L;

    // Atributos privados: encapsulamento (só se acessa pelos métodos)
    private final int codigo;   // identificador único; não muda depois de criado
    private String nome;        // nome do participante
    private long orcamento;     // orçamento em centavos (R$ 70,00 = 7000)

    /** Construtor: valida os dados antes de criar o participante. */
    public Participante(int codigo, String nome, long orcamento) throws PlanejamentoException {
        if (codigo <= 0) {
            throw new PlanejamentoException("O código deve ser maior que zero.");
        }
        validarNome(nome);
        validarOrcamento(orcamento);
        this.codigo = codigo;
        this.nome = nome.trim();    // remove espaços sobrando
        this.orcamento = orcamento;
    }

    /** Retorna o código do participante. */
    public int getCodigo() { return codigo; }

    /** Retorna o nome do participante. */
    public String getNome() { return nome; }

    /** Altera o nome, recusando valores vazios. */
    public void setNome(String nome) throws PlanejamentoException {
        validarNome(nome);
        this.nome = nome.trim();
    }

    /** Retorna o orçamento em centavos. */
    public long getOrcamento() { return orcamento; }

    /** Altera o orçamento, recusando valores negativos. */
    public void setOrcamento(long orcamento) throws PlanejamentoException {
        validarOrcamento(orcamento);
        this.orcamento = orcamento;
    }

    /** Regra: o nome não pode ser nulo nem vazio. */
    private static void validarNome(String nome) throws PlanejamentoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new PlanejamentoException("O nome não pode ficar vazio.");
        }
    }

    /** Regra: o orçamento deve ser maior ou igual a zero. */
    private static void validarOrcamento(long orcamento) throws PlanejamentoException {
        if (orcamento < 0) {
            throw new PlanejamentoException("O orçamento não pode ser negativo.");
        }
    }
}