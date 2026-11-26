package modelo;

/**
 * Exceção própria do projeto.
 * É lançada sempre que uma regra de negócio é violada
 * (nome vazio, orçamento negativo, código duplicado etc.).
 * A mensagem dela é exibida ao usuário na tela.
 */
public class PlanejamentoException extends Exception {
    // Identificador de versão da classe (exigido pela serialização)
    private static final long serialVersionUID = 1L;

    // Recebe a mensagem de erro e repassa para a classe Exception
    public PlanejamentoException(String mensagem) {
        super(mensagem);
    }
}