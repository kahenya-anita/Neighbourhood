package com.neighbourhood.online.neighbourhood.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NeighbourHoodPictureRequest {
    private String neighbourHoodId;
    private String neighbourHoodPicsUuid;
}
