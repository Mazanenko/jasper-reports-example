package com.example.reports.service;

import com.example.reports.entity.Document;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

public interface DocumentService {

    void createDocument(Document document) throws Exception;

    Document getDocumentByName(String name) throws Exception;

    void updateDocumentById(Long id, Document updatedDocument);

    void updateDocumentByName(String name, Document updatedDocument);

    void deleteDocumentByName(String name);

    Map<String, Object> findAll(String filter, Integer page, Integer size, String sort);

    List<Map<String, Object>> report();
}
