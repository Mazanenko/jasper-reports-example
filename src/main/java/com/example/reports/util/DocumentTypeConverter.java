package com.example.reports.util;

import com.example.reports.entity.DocumentType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class DocumentTypeConverter implements AttributeConverter<DocumentType, String> {
    @Override
    public String convertToDatabaseColumn(DocumentType type) {
        if (type == null) {
            return null;
        }
        return type.getCode();
    }

    @Override
    public DocumentType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(DocumentType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
