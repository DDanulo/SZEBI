package com.example.server.Simulation.data;

import com.example.server.Simulation.entities.EnergyProducingDevice;
import com.example.server.Simulation.entities.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

//to bedzie klasa generyczna ale najpierw musze dogaca jak sie z baza łaczyc i czy moge
/// abstrachujac od faktu e to chyba ine bedzie JpaReposiroty jesli to ma w takim układzie pozostac
public interface SolarPanelRepository extends JpaRepository<SolarPanel,  UUID> {


    //czemu tu zwraca boolean?????



    /// i po jasny pieron jest save to DB jak jest add????
    /// jak to jest przecie repo w bazie a nie in-memory?



}
