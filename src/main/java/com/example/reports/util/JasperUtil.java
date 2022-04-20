package com.example.reports.util;


import com.example.reports.entity.Document;
import com.example.reports.entity.Template;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface JasperUtil {

    List<Map<String, Object>> dataCollection(Document document);

    List<Map<String, Object>> dataCollection(List<Document> documentList);

    Template findTemplate(String templateName, Document document);

    File findTemplateFile(Template template);
}
