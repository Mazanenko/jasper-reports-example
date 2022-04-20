package com.example.reports.entity;

import com.example.reports.util.DocumentTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.Objects;


@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Convert(converter = DocumentTypeConverter.class)
    @Column(name = "type")
    private DocumentType type;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    @Column(name = "customer")
    private String customer;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "created", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdDateWithTimeZone;

    @Column(name = "modified", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime modifiedDateWithTimeZone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id.equals(document.id)
                && name.equals(document.name)
                && type == document.type
                && Objects.equals(content, document.content)
                && status.equals(document.status)
                && Objects.equals(customer, document.customer)
                && Objects.equals(supplier, document.supplier)
                && createdDateWithTimeZone.equals(document.createdDateWithTimeZone)
                && Objects.equals(modifiedDateWithTimeZone, document.modifiedDateWithTimeZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, content, status, customer, supplier, createdDateWithTimeZone
                , modifiedDateWithTimeZone);
    }
}
