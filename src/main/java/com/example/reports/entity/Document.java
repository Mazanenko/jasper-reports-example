package com.example.reports.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;


@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "number")
    private Long number;

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
}
