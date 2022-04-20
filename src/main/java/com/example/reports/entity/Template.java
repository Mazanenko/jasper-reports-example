package com.example.reports.entity;

import com.example.reports.util.DocumentTypeConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "template")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "link")
    @NotBlank
    private String link;

    @Convert(converter = DocumentTypeConverter.class)
    @Column(name = "type")
    @NotNull
    private DocumentType documentType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (!id.equals(template.id)) return false;
        if (!name.equals(template.name)) return false;
        if (!link.equals(template.link)) return false;
        return documentType == template.documentType;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + documentType.hashCode();
        return result;
    }
}
