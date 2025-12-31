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
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;

@Service
public class PdfReportGenerator {

    public byte[] generateUsageReport(List<DataPoint> data, LocalDateTime from, LocalDateTime to,
                                      Map<String, String> deviceAliases,
                                      String granularity) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // nagłówek
            String fontPath = "fonts/arial.ttf";
            Font titleFont = FontFactory.getFont(fontPath, BaseFont.IDENTITY_H,BaseFont.EMBEDDED, 18);
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

            // wykres słupkowy (agregacja wg granularności)
            document.add(new Paragraph(" "));
            Font chartTitleFont = FontFactory.getFont(fontPath,BaseFont.IDENTITY_H,BaseFont.EMBEDDED, 16);
            String chartTitle;
            if ("hourly".equalsIgnoreCase(granularity)) {
                chartTitle = "Wykres godzinowy (słupkowy)";
            } else if ("daily".equalsIgnoreCase(granularity)) {
                chartTitle = "Wykres dzienny (słupkowy)";
            } else if ("weekly".equalsIgnoreCase(granularity)) {
                chartTitle = "Wykres tygodniowy (słupkowy)";
            } else if ("monthly".equalsIgnoreCase(granularity)) {
                chartTitle = "Wykres miesięczny (słupkowy)";
            } else if ("yearly".equalsIgnoreCase(granularity)) {
                chartTitle = "Wykres roczny (słupkowy)";
            } else {
                chartTitle = "Wykres (słupkowy)";
            }
            document.add(new Paragraph(chartTitle, chartTitleFont));
            document.add(new Paragraph(" "));

            if (data == null || data.isEmpty()) {
                document.add(new Paragraph("Brak danych do narysowania wykresu."));
            } else {
                boolean hourly = "hourly".equalsIgnoreCase(granularity);
                boolean daily = "daily".equalsIgnoreCase(granularity) ||
                        (!"hourly".equalsIgnoreCase(granularity)
                                && !"weekly".equalsIgnoreCase(granularity)
                                && !"monthly".equalsIgnoreCase(granularity)
                                && !"yearly".equalsIgnoreCase(granularity));
                boolean weekly = "weekly".equalsIgnoreCase(granularity);
                boolean monthly = "monthly".equalsIgnoreCase(granularity);
                boolean yearly = "yearly".equalsIgnoreCase(granularity);

                Map<String, Double> buckets;
                if (hourly) {
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> dp.getTimestamp().withMinute(0).withSecond(0).withNano(0).toString(),
                            Collectors.summingDouble(DataPoint::getValue)));
                } else if (weekly) {
                    java.time.temporal.WeekFields wf = java.time.temporal.WeekFields.ISO;
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> {
                                var ts = dp.getTimestamp();
                                int y = ts.get(wf.weekBasedYear());
                                int w = ts.get(wf.weekOfWeekBasedYear());
                                return String.format("%04d-W%02d", y, w);
                            },
                            Collectors.summingDouble(DataPoint::getValue)));
                } else if (monthly) {
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> {
                                var ts = dp.getTimestamp();
                                int y = ts.getYear();
                                int m = ts.getMonthValue();
                                return String.format("%04d-%02d", y, m);
                            },
                            Collectors.summingDouble(DataPoint::getValue)));
                } else if (yearly) {
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> String.format("%04d", dp.getTimestamp().getYear()), // YYYY
                            Collectors.summingDouble(DataPoint::getValue)));
                } else if (daily) {
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> dp.getTimestamp().toLocalDate().toString(),
                            Collectors.summingDouble(DataPoint::getValue)));
                } else {
                    buckets = data.stream().collect(Collectors.groupingBy(
                            dp -> dp.getTimestamp().toLocalDate().toString(),
                            Collectors.summingDouble(DataPoint::getValue)));
                }

                List<String> keys = buildFullKeys(from, to, hourly, daily, weekly, monthly, yearly);

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

                    String lbl;
                    if (hourly) {
                        // key jest w formacie ISO_LOCAL_DATE_TIME
                        String hour = key.substring(11, 16);
                        String day = key.substring(5, 10);
                        lbl = day + " " + hour; // MM-dd HH:mm
                    } else if (weekly) {
                        // format YYYY-Www
                        lbl = key;
                    } else if (monthly) {
                        // format YYYY-MM
                        lbl = key;
                    } else if (yearly) {
                        // format YYYY
                        lbl = key;
                    } else {
                        // format YYYY-MM-DD, a ma pokazać MM-DD
                        lbl = key.substring(5);
                    }
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

    // budowanie pełnej listy kluczy osi X dla wybranego przedziału czasu i granularności
    private List<String> buildFullKeys(LocalDateTime from, LocalDateTime to,
                                       boolean hourly, boolean daily, boolean weekly, boolean monthly, boolean yearly) {
        List<String> keys = new ArrayList<>();
        if (hourly) {
            LocalDateTime cur = from.withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = to.withMinute(0).withSecond(0).withNano(0);
            while (!cur.isAfter(end)) {
                keys.add(cur.toString());
                cur = cur.plusHours(1);
            }
            return keys;
        }
        if (weekly) {
            LocalDate startDate = from.toLocalDate();

            LocalDate cur = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endDate = to.toLocalDate();
            java.time.temporal.WeekFields wf = java.time.temporal.WeekFields.ISO;
            while (!cur.isAfter(endDate)) {
                int y = cur.get(wf.weekBasedYear());
                int w = cur.get(wf.weekOfWeekBasedYear());
                String key = String.format("%04d-W%02d", y, w);
                if (keys.isEmpty() || !keys.get(keys.size() - 1).equals(key)) {
                    keys.add(key);
                }
                cur = cur.plusWeeks(1);
            }
            int yEnd = endDate.get(wf.weekBasedYear());
            int wEnd = endDate.get(wf.weekOfWeekBasedYear());
            String lastKey = String.format("%04d-W%02d", yEnd, wEnd);
            if (keys.isEmpty() || !keys.get(keys.size() - 1).equals(lastKey)) {
                keys.add(lastKey);
            }
            return keys;
        }
        if (monthly) {
            YearMonth start = YearMonth.from(from);
            YearMonth end = YearMonth.from(to);
            YearMonth cur = start;
            while (!cur.isAfter(end)) {
                keys.add(String.format("%04d-%02d", cur.getYear(), cur.getMonthValue()));
                cur = cur.plusMonths(1);
            }
            return keys;
        }
        if (yearly) {
            int y = from.getYear();
            int yEnd = to.getYear();
            for (int yy = y; yy <= yEnd; yy++) {
                keys.add(String.format("%04d", yy));
            }
            return keys;
        }
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