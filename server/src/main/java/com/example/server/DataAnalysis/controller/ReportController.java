package com.example.server.DataAnalysis.controller;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import com.example.server.DataAnalysis.service.DataFetcher;
import com.example.server.DataAnalysis.service.PdfReportGenerator;
import com.example.server.DataAnalysis.service.DataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final DataFetcher dataFetcher;
    private final PdfReportGenerator pdfGenerator;
    private final DataProvider dataProvider;

    @GetMapping("/chart-data")
    public ResponseEntity<List<DataPoint>> getChartData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type
    ) {
        return ResponseEntity.ok(dataFetcher.fetchData(from, to, type));
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
            @RequestParam(required = false) Double insolation
    ) {
        List<DataPoint> data = dataFetcher.fetchData(from, to, DataType.ENERGY_CONSUMPTION);
        byte[] pdfBytes = pdfGenerator.generateUsageReport(data, from, to);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=raport_zuzycia.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/aggregated")
    public ResponseEntity<Map<String, Double>> getAggregated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam DataType type
    ) {
        return ResponseEntity.ok(dataProvider.getAggregatedData(from, to, type));
    }

    @GetMapping("/last-updated")
    public ResponseEntity<LocalDateTime> getLastUpdated() {
        return ResponseEntity.ok(dataProvider.getLastUpdated());
    }

    // endpoint pozwalający sprawdzić, czy aktualnie włączony jest tryb mockowania danych
    @GetMapping("/mock-status")
    public ResponseEntity<Boolean> getMockStatus() {
        return ResponseEntity.ok(dataFetcher.isMockEnabled());
    }
}