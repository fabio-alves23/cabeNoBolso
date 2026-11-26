package persistencia;

import modelo.Planejamento;
import modelo.PlanejamentoException;

/**
 * Contrato de persistência: define O QUE fazer (salvar e carregar),
 * sem dizer COMO. Quem usa o programa depende só deste contrato.
 */
public interface Persistencia {

    /** Salva o planejamento completo. */
    void salvar(Planejamento planejamento) throws PlanejamentoException;

    /** Carrega um planejamento salvo e o devolve como um objeto novo. */
    Planejamento carregar() throws PlanejamentoException;
}