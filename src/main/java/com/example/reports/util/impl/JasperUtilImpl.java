package com.example.reports.util.impl;

import com.example.reports.entity.Document;
import com.example.reports.entity.DocumentType;
import com.example.reports.entity.Template;
import com.example.reports.repository.TemplateRepo;
import com.example.reports.util.JasperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JasperUtilImpl implements JasperUtil {

    private final TemplateRepo templateRepo;

    @Override
    public List<Map<String, Object>> dataCollection(Document document) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item = fillMap(document);
        data.add(item);
        return data;
    }

    @Override
    public List<Map<String, Object>> dataCollection(List<Document> documentList) {
        List<Map<String, Object>> result = new ArrayList<>();

        documentList.forEach(document -> {
            Map<String, Object> item = fillMap(document);
            result.add(item);
        });
        return result;
    }

    @Override
    public Template findTemplate(String templateName, Document document) {

        if (templateName == null || templateName.isBlank()) {
            log.info("Given template is empty or null. " +
                    "Trying to find any matchable template for doc ({})", document.getName());

            return findFirstFromDbByType(document.getType());
        }

        Template temp = templateRepo.findByName(templateName).orElseThrow(() -> {
            log.warn("Can't found template with type ({})", templateName);
            throw new EntityNotFoundException(String.format("Can't found template with type (%s)",
                    templateName));
        });

        if (temp.getDocumentType().equals(document.getType())) {
            return temp;
        } else {
            log.warn("Mismatch document type ({}) and template type ({}) for template ({})",
                    document.getType(), temp.getDocumentType(), templateName);
            log.info("Trying to find any matchable template for doc ({})", document.getName());
            return findFirstFromDbByType(document.getType());
        }
    }

    @Override
    public File findTemplateFile(Template template) {
        File file;
        try {
            file = ResourceUtils.getFile(template.getLink());
        } catch (FileNotFoundException e) {
            log.error("Can't find template ({}) from path: {}", template.getName(), template.getLink());
            throw new RuntimeException(e);
        }
        return file;
    }

    private Map<String, Object> fillMap(Document document) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", document.getId());
        map.put("name", document.getName());
        map.put("content", document.getContent());
        map.put("status", document.getStatus());
        map.put("customer", document.getCustomer());
        map.put("supplier", document.getSupplier());
        map.put("created", document.getCreatedDateWithTimeZone());
        map.put("modified", document.getModifiedDateWithTimeZone());
        return map;
    }

    private Template findFirstFromDbByType(DocumentType type) {
        return templateRepo.findFirstByDocumentType(type).orElseThrow(() -> {
            log.warn("Can't found template with type ({})", type.getCode());
            throw new EntityNotFoundException(String.format("Can't found any template with type (%s)",
                    type.getCode()));
        });
    }
}
