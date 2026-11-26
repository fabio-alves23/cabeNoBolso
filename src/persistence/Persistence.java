package persistence;

import model.TripPlan;
import model.PlanningException;

public interface Persistence {

    // Salva o planejamento completo
    void save(TripPlan plan) throws PlanningException;

    // Carrega um planejamento salvo
    TripPlan load() throws PlanningException;
}
