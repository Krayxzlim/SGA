package com.sga.service;

import java.io.ByteArrayOutputStream;
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
    private final AgendaRepository AgendaRepository;

    public ReportService(AgendaRepository AgendaRepository) {
        this.AgendaRepository = AgendaRepository;
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

            List<Agenda> Agendas = AgendaRepository.findAll();
            int rowIdx = 1;

            for (Agenda s : Agendas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getFecha());
                row.createCell(2).setCellValue(s.getHora());
                row.createCell(3).setCellValue(s.getTaller().getNombre());
                row.createCell(4).setCellValue(s.getResponsable().getNombre());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error exportando Excel", e);
        }
    }
}
