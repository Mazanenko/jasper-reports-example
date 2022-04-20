package com.example.reports.service.impl;

import com.example.reports.entity.Document;
import com.example.reports.entity.Template;
import com.example.reports.repository.DocumentRepo;
import com.example.reports.service.ReportService;
import com.example.reports.util.JasperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Value("${custom.defaultFormat}")
    private String defaultFormat;

    private final DocumentRepo documentRepo;
    private final JasperUtil jasperUtil;


    @Override
    public void makeReport(String documentName, String format, String templateName, HttpServletResponse response) {
        Document document = documentRepo.findByName(documentName).orElseThrow(() -> {
            log.warn("Can't found document with name ({})", documentName);
            throw new EntityNotFoundException("Can't found document");
        });
        Template template = jasperUtil.findTemplate(templateName, document);

        if (format == null || format.isBlank()) {
            log.warn("Given format is empty or null. Set default format ({})", defaultFormat);
            format = defaultFormat;
        }
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasperUtil.dataCollection(document));

        try (InputStream reportStream = new FileInputStream(jasperUtil.findTemplateFile(template));
             OutputStream outputStream = response.getOutputStream()) {

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            switch (format.toLowerCase()) {
                case "pdf" -> {
                    response.setContentType("application/pdf");
                    response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                }
                case "html" -> {
                    response.setContentType("application/html");
                    response.addHeader("Content-Disposition", "inline; filename=jasper.html;");

                    HtmlExporter exporter = new HtmlExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                    exporter.exportReport();
                }
                case "csv" -> {
                    response.setContentType("application/csv");
                    JRCsvExporter exporter = new JRCsvExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                    exporter.exportReport();
                }
                case "xlsx" -> {
                    response.setContentType("application/xlsx");
                    response.addHeader("Content-Disposition", "inline; filename=" + documentName + ".xlsx;");

                    JRXlsxExporter exporter = new JRXlsxExporter();
                    SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
                    reportConfig.setSheetNames(new String[]{"Data"});

                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.setConfiguration(reportConfig);
                    exporter.exportReport();
                }
                case "xml" -> {
                    response.setContentType("application/xml");
                    response.addHeader("Content-Disposition", "inline; filename=" + documentName + ".xml;");
                    JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
                }
                default -> log.warn("Given wrong format ({})", format);
            }
        } catch (IOException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
