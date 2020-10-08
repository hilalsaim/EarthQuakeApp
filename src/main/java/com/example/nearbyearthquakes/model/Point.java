package com.example.nearbyearthquakes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Point {
    long[] coordinates;
}
