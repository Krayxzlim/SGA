package com.sga.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.sga.model.Agenda;
import com.sga.repository.AgendaRepository;

@Service
public class ReportService {
    private final AgendaRepository agendaRepository;

    public ReportService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public byte[] exportAgendasToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Agenda");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Fecha");
            header.createCell(2).setCellValue("Hora");
            header.createCell(3).setCellValue("Taller");
            header.createCell(4).setCellValue("Tallerista");

            List<Agenda> agendas = agendaRepository.findAll();
            int rowIdx = 1;

            // Formatos para fecha y hora
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            for (Agenda s : agendas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getId());

                // Convertimos LocalDate a String
                if (s.getFecha() != null) {
                    row.createCell(1).setCellValue(s.getFecha().format(dateFormatter));
                }

                // Convertimos LocalTime a String
                if (s.getHora() != null) {
                    row.createCell(2).setCellValue(s.getHora().format(timeFormatter));
                }

                row.createCell(3).setCellValue(
                    s.getTaller() != null ? s.getTaller().getNombre() : ""
                );
                row.createCell(4).setCellValue(
                    s.getColegio() != null ? s.getColegio().getNombre() : ""
                );
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error exportando Excel", e);
        }
    }
}
