package com.neighbourhood.online.neighbourhood.models;

import com.neighbourhood.online.neighbourhood.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeighbourHood extends DateAudit {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String neighbourHoodId;

    @Column(unique = true)
    private String neighbourHoodName;

    private String neighbourHoodLocation;

    private int occupantCount = 0;

    @OneToOne
    private User admin;

    @ElementCollection
    private List<String> neighbourHoodPicsUuids = new ArrayList<>();
}