package persistencia;

import modelo.Planejamento;
import modelo.PlanejamentoException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implementa a persistência salvando o objeto inteiro em um arquivo local
 * (serialização). Os vínculos entre participantes e despesas são preservados.
 */
public class PersistenciaArquivo implements Persistencia {

    // Caminho do arquivo (ex.: "planejamento.dat")
    private final String caminho;

    public PersistenciaArquivo(String caminho) {
        this.caminho = caminho;
    }

    /** Grava o planejamento no arquivo. O try-with-resources fecha o fluxo sozinho. */
    @Override
    public void salvar(Planejamento planejamento) throws PlanejamentoException {
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(caminho))) {
            saida.writeObject(planejamento);
        } catch (IOException e) {
            throw new PlanejamentoException("Não foi possível salvar o arquivo: " + e.getMessage());
        }
    }

    /**
     * Lê o planejamento do arquivo e o devolve.
     * Em caso de falha, lança erro e quem chamou continua com o planejamento atual intacto.
     */
    @Override
    public Planejamento carregar() throws PlanejamentoException {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(caminho))) {
            return (Planejamento) entrada.readObject();
        } catch (FileNotFoundException e) {
            throw new PlanejamentoException("Arquivo não encontrado: " + caminho);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PlanejamentoException("O arquivo está inválido ou corrompido.");
        }
    }
}