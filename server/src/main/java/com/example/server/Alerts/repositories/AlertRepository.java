package com.example.server.Alerts.repositories;

import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByLevel(AlertLevel level);

    //List<Alert> getAllByTimestampBefore(LocalDateTime timestampBefore);
    
}
