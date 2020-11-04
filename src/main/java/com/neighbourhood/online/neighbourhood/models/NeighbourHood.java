package com.neighbourhood.online.neighbourhood.models;

import com.neighbourhood.online.neighbourhood.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
}