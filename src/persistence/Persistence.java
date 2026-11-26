package persistence;

import model.TripPlan;
import model.PlanningException;

public interface Persistence {

    // Saves the complete plan
    void save(TripPlan plan) throws PlanningException;

    // Loads a saved plan
    TripPlan load() throws PlanningException;
}
