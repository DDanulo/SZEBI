package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;

@Service
public class PdfReportGenerator {

    public byte[] generateUsageReport(List<DataPoint> data, LocalDateTime from, LocalDateTime to) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // nagłówek
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Raport Zużycia Energii", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Wygenerowano: " + LocalDateTime.now()));
            document.add(new Paragraph("Okres: " + from + " - " + to));

            document.add(new Paragraph(" "));

            // tabela z danymi
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

            // podsumowanie
            Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Całkowite zużycie: " + String.format("%.2f", total) + " kWh", summaryFont));

            // wykres słupkowy (agregacja dzienna)
            document.add(new Paragraph(" "));
            Font chartTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Wykres dzienny (słupkowy)", chartTitleFont));
            document.add(new Paragraph(" "));

            if (data == null || data.isEmpty()) {
                document.add(new Paragraph("Brak danych do narysowania wykresu."));
            } else {
                Map<LocalDate, Double> daily = data.stream()
                        .collect(Collectors.groupingBy(dp -> dp.getTimestamp().toLocalDate(), Collectors.summingDouble(DataPoint::getValue)));

                List<LocalDate> days = new ArrayList<>(daily.keySet());
                Collections.sort(days);

                double maxVal = daily.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
                if (maxVal <= 0) maxVal = 1.0;

                //obszar rysowania
                float left = 50f;
                float right = document.getPageSize().getRight(36f);
                float width = right - left - 20f;
                float bottom = 330f;
                float height = 220f;

                //osie
                var cb = writer.getDirectContent();
                cb.setLineWidth(0.8f);
                cb.setGrayStroke(0);
                // OX
                cb.moveTo(left, bottom);
                cb.lineTo(left + width, bottom);
                // OY
                cb.moveTo(left, bottom);
                cb.lineTo(left, bottom + height);
                cb.stroke();

                // słupki
                int n = days.size();
                float barGap = 6f;
                float barWidth = Math.max(8f, (width - (n + 1) * barGap) / Math.max(1, n));
                float x = left + barGap;
                cb.setColorFill(new Color(100, 149, 237));

                Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

                for (int i = 0; i < n; i++) {
                    LocalDate day = days.get(i);
                    double val = daily.getOrDefault(day, 0.0);
                    float barHeight = (float) (val / maxVal) * (height - 20f);

                    cb.rectangle(x, bottom + 0.5f, barWidth, Math.max(0.5f, barHeight));
                    cb.fill();

                    String lbl = day.getMonthValue() + "-" + String.format("%02d", day.getDayOfMonth());
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(lbl, labelFont), x + barWidth / 2f, bottom - 10f, 0);

                    if (n <= 14 || i % Math.max(1, n / 10) == 0) {
                        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                                new Phrase(String.format("%.0f", val), labelFont),
                                x + barWidth / 2f, bottom + barHeight + 6f, 0);
                    }

                    x += barWidth + barGap;
                }

                Font axisFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
                for (int i = 0; i <= 4; i++) {
                    float frac = i / 4f;
                    float y = bottom + frac * (height - 20f);
                    double tickVal = maxVal * frac;
                    cb.setGrayStroke(0.7f);
                    cb.setLineWidth(0.3f);
                    cb.moveTo(left, y);
                    cb.lineTo(left + width, y);
                    cb.stroke();
                    ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                            new Phrase(String.format("%.0f", tickVal), axisFont),
                            left - 6f, y - 2f, 0);
                }
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
    }
}