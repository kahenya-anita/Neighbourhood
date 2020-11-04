package com.neighbourhood.online.neighbourhood.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {

    private String businessName;

    @ManyToOne
    private String businessOwnerId;

    private String emailAddress;

    @OneToOne
    private String neighbourHoodId;
}
