package com.example.reports.controller;


import com.example.reports.entity.Document;
import com.example.reports.service.impl.DocumentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentServiceImpl documentService;

    @Operation(summary = "Get a document by it's name")
    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> getDocumentByName(@PathVariable("name") String name) {
        Document document;
        try {
            document = documentService.getDocumentByName(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return new ResponseEntity<>(document, HttpStatus.OK);
    }


    @Operation(summary = "Get all documents")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(value = "filter", required = false) String filter,
                                                       @RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "size", required = false) Integer size,
                                                       @RequestParam(value = "sort", required = false) String sort) {
        try {
            return new ResponseEntity<>(documentService.findAll(filter, page, size, sort), HttpStatus.OK);
        } catch (Exception e) {
            // temp. Need bunch of custom exceptions
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad request");
        }
    }


    @Operation(summary = "Create document")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createDoc(@RequestBody @Valid Document document, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream().collect(Collectors.toMap(FieldError::getField,
                            fieldError -> Objects.requireNonNull(fieldError.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            documentService.createDocument(document);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }


    @Operation(summary = "Update whole document by it's name")
    @PutMapping(value = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateDocumentByName(@PathVariable("name") String name,
                                                       @RequestBody Document document) {
        if (name == null || name.isBlank()) {
            return new ResponseEntity<>("bad name", HttpStatus.BAD_REQUEST);
        }
        documentService.updateDocumentByName(name, document);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Delete document")
    @DeleteMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteDocByName(@PathVariable("name") String name) {
        if (name == null || name.isBlank()) {
            return new ResponseEntity<>("bad name", HttpStatus.BAD_REQUEST);
        }
        documentService.deleteDocumentByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/pdf")
    public void report(HttpServletResponse response) throws JRException, IOException {
        response.setContentType("application/x-download");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(documentService.report());
        InputStream reportStream = getClass().getResourceAsStream("/report1.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRSaver.saveObject(jasperReport, "employeeReport.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        OutputStream outputStream =response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");




//        response.setContentType("text/html");
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(documentService.report());
//        InputStream inputStream = this.getClass().getResourceAsStream("report1.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
//        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//        exporter.exportReport();
    }
}
