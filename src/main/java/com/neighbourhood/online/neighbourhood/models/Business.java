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
public class Business extends DateAudit{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String neighbourHoodBusinessId;

    @Column(unique = true)
    private String businessName;

    @ManyToOne
    private User businessOwner;

    private String emailAddress;

    @OneToOne
    private NeighbourHood neighbourHood;
}