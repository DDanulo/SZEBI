package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
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

    public byte[] generateUsageReport(List<DataPoint> data, LocalDateTime from, LocalDateTime to,
                                      Map<String, String> deviceAliases) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // nagłówek
            String fontPath = "fonts/arial.ttf";
            Font titleFont = FontFactory.getFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18);
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
                String devId = dp.getDeviceId();
                String alias = deviceAliases != null && devId != null ? deviceAliases.get(devId) : null;
                table.addCell(alias != null && !alias.isBlank() ? alias : (devId != null ? devId : "-"));
                table.addCell(String.format("%.2f", dp.getValue()));
                total += dp.getValue();
            }

            document.add(table);

            // podsumowanie
            Font summaryFont = FontFactory.getFont(fontPath,BaseFont.IDENTITY_H,BaseFont.EMBEDDED, 14);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Całkowite zużycie: " + String.format("%.2f", total) + " kWh", summaryFont));

            // TUTAJ DODAJEMY NOWĄ STRONĘ, ABY WYKRES NIE NACHODZIŁ NA TABELĘ
            document.newPage();

            // wykres słupkowy
            document.add(new Paragraph(" "));
            Font chartTitleFont = FontFactory.getFont(fontPath,BaseFont.IDENTITY_H,BaseFont.EMBEDDED, 16);
            String chartTitle = "Wykres słupkowy";
            document.add(new Paragraph(chartTitle, chartTitleFont));
            document.add(new Paragraph(" "));

            if (data.isEmpty()) {
                document.add(new Paragraph("Brak danych do narysowania wykresu."));
            } else {
                // Pozostawiono agregację dzienną
                Map<String, Double> buckets = data.stream().collect(Collectors.groupingBy(
                        dp -> dp.getTimestamp().toLocalDate().toString(),
                        Collectors.summingDouble(DataPoint::getValue)));

                List<String> keys = buildFullKeys(from, to);

                List<Double> values = new ArrayList<>(keys.size());
                for (String k : keys) {
                    values.add(buckets.getOrDefault(k, 0.0));
                }

                // przycinamy wiodące i końcowe zera
                int firstNonZero = -1;
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) != null && values.get(i) > 0.0) { firstNonZero = i; break; }
                }
                int lastNonZero = -1;
                for (int i = values.size() - 1; i >= 0; i--) {
                    if (values.get(i) != null && values.get(i) > 0.0) { lastNonZero = i; break; }
                }

                if (firstNonZero == -1 || lastNonZero == -1 || firstNonZero > lastNonZero) {
                    document.add(new Paragraph("Brak niezerowych danych w zadanym zakresie."));
                    document.close();
                    return out.toByteArray();
                }

                keys = new ArrayList<>(keys.subList(firstNonZero, lastNonZero + 1));
                values = new ArrayList<>(values.subList(firstNonZero, lastNonZero + 1));

                //maksymalna wartość do skalowania liczona z przyciętego zakresu
                double maxVal = values.stream().mapToDouble(v -> v != null ? v : 0.0).max().orElse(1.0);
                if (maxVal <= 0) maxVal = 1.0;

                //obszar rysowania
                float left = 50f;
                float right = document.getPageSize().getRight(36f);
                float width = right - left - 20f;
                float bottom = 450f; // Podniesiono wykres wyżej na nowej stronie
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
                int n = keys.size();
                float barGap = 6f;
                float barWidth = Math.max(8f, (width - (n + 1) * barGap) / Math.max(1, n));
                float x = left + barGap;
                cb.setColorFill(new Color(100, 149, 237));

                Font labelFont = FontFactory.getFont(fontPath,BaseFont.IDENTITY_H,BaseFont.EMBEDDED, 8);

                for (int i = 0; i < n; i++) {
                    String key = keys.get(i);
                    double val = values.get(i);
                    float barHeight = (float) (val / maxVal) * (height - 20f);

                    cb.rectangle(x, bottom + 0.5f, barWidth, Math.max(0.5f, barHeight));
                    cb.fill();

                    // format YYYY-MM-DD, a ma pokazać MM-DD
                    String lbl = key.substring(5);

                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(lbl, labelFont), x + barWidth / 2f, bottom - 10f, 0);

                    if (n <= 14 || i % Math.max(1, n / 10) == 0) {
                        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                                new Phrase(String.format("%.0f", val), labelFont),
                                x + barWidth / 2f, bottom + barHeight + 6f, 0);
                    }

                    x += barWidth + barGap;
                }

                Font axisFont = FontFactory.getFont(fontPath,BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8);
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

    // budowanie pełnej listy kluczy osi X dla wybranego przedziału czasu
    private List<String> buildFullKeys(LocalDateTime from, LocalDateTime to) {
        List<String> keys = new ArrayList<>();
        // daily (domyślnie)
        LocalDate cur = from.toLocalDate();
        LocalDate end = to.toLocalDate();
        while (!cur.isAfter(end)) {
            keys.add(cur.toString());
            cur = cur.plusDays(1);
        }
        return keys;
    }
}