package com.example.server.Alerts.repositories;

import com.example.server.Alerts.entities.AlertRule;
import com.example.server.Alerts.entities.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {

    List<AlertRule> findByMetric(Metric metric);



}
