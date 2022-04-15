package com.example.reports.controller;


import com.example.reports.entity.Document;
import com.example.reports.service.impl.DocumentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
}
