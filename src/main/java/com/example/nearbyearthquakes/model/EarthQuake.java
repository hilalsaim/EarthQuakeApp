package com.example.nearbyearthquakes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EarthQuake {
    private String mag;
    private String title;
    private String place;
    @JsonIgnore
    private int distance = Integer.MAX_VALUE;
}
