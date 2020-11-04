package com.neighbourhood.online.neighbourhood.models;

import com.neighbourhood.online.neighbourhood.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "neighbourhood_user")
public class User extends DateAudit {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String neighbourHoodUserId;

    private String name;

    @Column(unique = true)
    private String emailAddress;

    @JsonIgnore
    private String password;

    @ManyToOne
    private NeighbourHood neighbourHood;
}
