package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PdfReportGenerator {

    public byte[] generateUsageReport(List<DataPoint> data, LocalDateTime from, LocalDateTime to) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            // Nagłówek
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Raport Zużycia Energii", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Wygenerowano: " + LocalDateTime.now()));
            document.add(new Paragraph("Okres: " + from + " - " + to));
            document.add(new Paragraph(" "));

            // Tabela z danymi
            com.lowagie.text.Table table = new com.lowagie.text.Table(3);
            table.setWidth(100);
            table.setPadding(4);

            table.addCell(new Cell("Data i Czas"));
            table.addCell(new Cell("Urządzenie"));
            table.addCell(new Cell("Wartość [kWh]"));

            double total = 0;
            for (DataPoint dp : data) {
                table.addCell(dp.getTimestamp().toString());
                table.addCell(dp.getDeviceId());
                table.addCell(String.format("%.2f", dp.getValue()));
                total += dp.getValue();
            }

            document.add(table);

            // Podsumowanie
            Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Całkowite zużycie: " + String.format("%.2f", total) + " kWh", summaryFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
    }
}