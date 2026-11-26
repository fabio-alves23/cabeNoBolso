
package persistence;

import model.TripPlan;
import model.PlanningException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FilePersistence implements Persistence {

    private final String filePath;

    // Define o caminho do arquivo utilizado para salvar os dados
    public FilePersistence(String filePath) {
        this.filePath = filePath;
    }

    @Override
    // Salva o planejamento completo no arquivo
    public void save(TripPlan plan) throws PlanningException {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            output.writeObject(plan);

        } catch (IOException e) {
            throw new PlanningException(
                    "Não foi possível salvar o arquivo: " + e.getMessage());
        }
    }

    @Override
    // Recupera o planejamento salvo no arquivo
    public TripPlan load() throws PlanningException {
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            return (TripPlan) input.readObject();

        } catch (FileNotFoundException e) {
            throw new PlanningException("Arquivo não encontrado: " + filePath);

        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PlanningException(
                    "O arquivo está inválido ou corrompido.");
        }
    }
}
