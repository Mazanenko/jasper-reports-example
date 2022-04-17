package com.example.reports.service.impl;

import com.example.reports.entity.Document;
import com.example.reports.repository.DocumentRepo;
import com.example.reports.service.DocumentService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    @Value("${custom.defaultSort}")
    private String defaultSort;

    @Value("${custom.defaultSize}")
    private Integer defaultSize;

    private final DocumentRepo documentRepo;

    @Override
    @Transactional
    public void createDocument(Document document) throws Exception {
        if (documentRepo.existsByName(document.getName())) {
            log.error("document with name ({}) already exist", document.getName());
            throw new Exception(String.format("Document with name (%s) already exist", document.getName()));
        }
        document.setCreatedDateWithTimeZone(OffsetDateTime.now());
        document.setModifiedDateWithTimeZone(null);
        documentRepo.save(document);
    }

    @Override
    public Document getDocumentByName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            log.warn("Given document's name is null or empty");
            throw new IllegalArgumentException("Given document's name is null or empty");
        }
        return documentRepo.findByName(name).orElseThrow(() -> {
            log.warn("Can't found document with name ({})", name);
            return new Exception("Can't found document");
        });
    }

    @Override
    @Transactional
    public void updateDocumentById(Long id, Document updatedDocument) {
        if (id == null || id <= 0) {
            log.error("Given Id is " + id + " Should be greater than 0");
            throw new IllegalArgumentException("Given Id is " + id + " Should be greater than 0");
        }
        if (updatedDocument == null) {
            log.error("Given updated document is null");
            throw new IllegalArgumentException("Given updated document is null");
        }

        Document documentFromDatabase = documentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't found document"));

        documentRepo.save(updateAllFields(documentFromDatabase, updatedDocument));
    }

    @Override
    @Transactional
    public void updateDocumentByName(String name, Document updatedDocument) {
        if (name == null || name.isBlank()) {
            log.error("Given name is {} null or blank", name);
            throw new IllegalArgumentException("Given name is " + name + " null or blank");
        }
        if (updatedDocument == null) {
            log.error("Given updated document is null");
            throw new IllegalArgumentException("Given updated document is null");
        }

        Document documentFromDatabase = documentRepo.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Can't found document"));

        documentRepo.save(updateAllFields(documentFromDatabase, updatedDocument));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentByName(String name) {
        if (name == null || name.isEmpty()) {
            log.error("Given document's name is null or empty");
            throw new IllegalArgumentException("Given document's name is null or empty");
        }
        if (documentRepo.existsByName(name)) {
            documentRepo.deleteByName(name);
        } else {
            log.warn("Document with name {} not found in db", name);
            throw new EntityNotFoundException(String.format("There is no document with name %s in data base", name));
        }
    }

    @Override
    public Map<String, Object> findAll(String filter, Integer page, Integer size, String sort) {
        Map<String, Object> responseEntity = new HashMap<>();
        Page<Document> documentPage;

        if (page == null || page < 0) {
            log.warn("Invalid page value (page = {}). Set default page value = 0", page);
            page = 0; //default page
        }
        if (sort == null || sort.isBlank()) {
            log.warn("Invalid sort value (sort = {}). Set default sort value = {}", sort, defaultSort);
            sort = defaultSort;    //default sort
        }
        if (size == null || size <= 0) {
            log.warn("Invalid size value (size = {}). Set default size  = {}", size, defaultSize);
            size = defaultSize;  //default size
        }

        responseEntity.put("filter", filter);
        responseEntity.put("sort", sort);
        responseEntity.put("page", page);
        responseEntity.put("elements on page", size);

        if (filter == null || filter.isBlank()) {
            documentPage = documentRepo.findAll(RSQLJPASupport.toSort(sort), PageRequest.of(page, size));
        } else {
            Specification<?> specification = RSQLJPASupport.toSpecification(filter)
                    .and(RSQLJPASupport.toSort(sort));


            //  https://www.baeldung.com/java-warning-unchecked-cast#:~:text=4.2.%20Suppress%20the%20%E2%80%9Cunchecked%E2%80%9D%20Warning
            @SuppressWarnings("unchecked")
            Page<Document> temp = documentRepo
                    .findAll((Specification<Document>) specification, PageRequest.of(page, size));
            documentPage = temp;
        }
        responseEntity.put("pages", documentPage.getTotalPages());
        responseEntity.put("entity", documentPage.stream().toList());
        return responseEntity;
    }

    @Override
    public List<Map<String, Object>> report() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Document> documents = documentRepo.findAll();
        documents.forEach(document -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", document.getId());
            item.put("amount", document.getAmount());
            item.put("name", document.getName());
            item.put("number", document.getNumber());
            item.put("status", document.getStatus());
            result.add(item);
        });
        return result;
    }

    private Document updateAllFields(Document documentFromDB, Document updatedDocument) {
        documentFromDB.setAmount(updatedDocument.getAmount());
        documentFromDB.setName(documentFromDB.getName());
        documentFromDB.setNumber(documentFromDB.getNumber());
        documentFromDB.setContent(updatedDocument.getContent());
        documentFromDB.setStatus(updatedDocument.getStatus());
        documentFromDB.setCustomer(documentFromDB.getCustomer());
        documentFromDB.setSupplier(updatedDocument.getSupplier());
        documentFromDB.setModifiedDateWithTimeZone(OffsetDateTime.now());
        return documentFromDB;
    }
}
