package com.example.reports.repository;

import com.example.reports.entity.DocumentType;
import com.example.reports.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {
    Optional<Template> findFirstByDocumentType(@NotNull DocumentType documentType);

    Optional<Template> findByName(String templateName);
}
