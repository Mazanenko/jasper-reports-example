package com.example.reports.repository;

import com.example.reports.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    Optional<Document> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}
