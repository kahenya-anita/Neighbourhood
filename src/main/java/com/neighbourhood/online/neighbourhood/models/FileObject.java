package com.neighbourhood.online.neighbourhood.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neighbourhood.online.neighbourhood.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileObject  extends DateAudit {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String fileId;

    private String fileName;

    private String fileType;

    @Lob
    @JsonIgnore
    private byte[] fileData;
}
