package com.example.server.DataAnalysis.controller;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import com.example.server.DataAnalysis.model.DeviceInfo;
import com.example.server.DataAnalysis.service.DataFetcher;
import com.example.server.DataAnalysis.service.PdfReportGenerator;
import com.example.server.DataAnalysis.service.DataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final DataFetcher dataFetcher;
    private final PdfReportGenerator pdfGenerator;
    private final DataProvider dataProvider;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/chart-data")
    public ResponseEntity<List<DataPoint>> getChartData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type,
            @RequestParam(required = false, name = "deviceIds") List<String> deviceIds
    ) {
        List<DataPoint> raw = dataFetcher.fetchData(from, to, type);
        if (deviceIds != null && !deviceIds.isEmpty()) {
            raw = raw.stream()
                    .filter(dp -> dp.getDeviceId() != null && deviceIds.contains(dp.getDeviceId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(raw);
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            // parametry z modułu symulacji
            @RequestParam(required = false) String season,
            @RequestParam(required = false, name = "dayTime") String dayTime,
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Double windSpeed,
            @RequestParam(required = false) Double insolation,
            @RequestParam(required = false, name = "deviceIds") List<String> deviceIds,
            @RequestParam(required = false, defaultValue = "daily") String granularity
    ) {
        List<DataPoint> data = dataFetcher.fetchData(from, to, DataType.ENERGY_CONSUMPTION);
        if (deviceIds != null && !deviceIds.isEmpty()) {
            data = data.stream()
                    .filter(dp -> dp.getDeviceId() != null && deviceIds.contains(dp.getDeviceId()))
                    .collect(Collectors.toList());
        }
        // mapa aliasów (to ta dupa XD) urządzeń
        String sql = "SELECT id, description FROM device";
        Map<String, String> aliasMap = jdbcTemplate.query(sql, (rs) -> {
            Map<String, String> map = new java.util.HashMap<>();
            while (rs.next()) {
                Object idObj = rs.getObject("id");
                String id = java.util.Objects.toString(idObj, null);
                String desc = rs.getString("description");
                if (id != null) map.put(id, desc);
            }
            return map;
        });

        byte[] pdfBytes = pdfGenerator.generateUsageReport(data, from, to, aliasMap, granularity);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=raport_zuzycia.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/aggregated")
    public ResponseEntity<Map<String, Double>> getAggregated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type,
            @RequestParam(required = false, name = "deviceIds") List<String> deviceIds
    ) {
        List<DataPoint> data = dataProvider.getRawData(from, to, type);
        if (deviceIds != null && !deviceIds.isEmpty()) {
            data = data.stream()
                    .filter(dp -> dp.getDeviceId() != null && deviceIds.contains(dp.getDeviceId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(dataProvider.aggregateByDay(data));
    }

    @GetMapping("/average")
    public ResponseEntity<Double> getAverage(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type,
            @RequestParam(required = false, name = "deviceIds") List<String> deviceIds
    ) {
        List<DataPoint> data = dataProvider.getRawData(from, to, type);
        if (deviceIds != null && !deviceIds.isEmpty()) {
            data = data.stream()
                    .filter(dp -> dp.getDeviceId() != null && deviceIds.contains(dp.getDeviceId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(dataProvider.calculateAverage(data));
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type,
            @RequestParam(required = false, name = "deviceIds") List<String> deviceIds
    ) {
        List<DataPoint> data = dataProvider.getRawData(from, to, type);
        if (deviceIds != null && !deviceIds.isEmpty()) {
            data = data.stream()
                    .filter(dp -> dp.getDeviceId() != null && deviceIds.contains(dp.getDeviceId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(dataProvider.calculateTotal(data));
    }

    @GetMapping("/last-updated")
    public ResponseEntity<LocalDateTime> getLastUpdated() {
        return ResponseEntity.ok(dataProvider.getLastUpdated());
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceInfo>> listDevices() {
        String sql = "SELECT id, description, type FROM device ORDER BY description";
        List<DeviceInfo> devices = jdbcTemplate.query(sql, (rs, rowNum) -> new DeviceInfo(
                Objects.toString(rs.getObject("id"), null),
                rs.getString("description"),
                rs.getString("type")
        ));
        return ResponseEntity.ok(devices);
    }
}