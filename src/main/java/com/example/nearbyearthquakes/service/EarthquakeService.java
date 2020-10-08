package com.example.nearbyearthquakes.service;

import com.example.nearbyearthquakes.model.Feature;
import com.example.nearbyearthquakes.model.FeatureCollection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

@Service
@Slf4j
public class EarthquakeService {

    private static final String EARTHQUAKE_API_URI = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    public List<Feature> getEarthquakeList(String latitude, String longitude, String maxRatio) throws JsonProcessingException, URISyntaxException {

        URI uri = createURIForAPI(latitude, longitude, maxRatio);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        FeatureCollection featureCollection =
                objectMapper.readValue(response.getBody(), FeatureCollection.class);

        for(Feature feature : featureCollection.getFeatures()) {
            double x2 = feature.getGeometry().getCoordinates()[0];
            double y2 = feature.getGeometry().getCoordinates()[1];
            double x1 = Double.valueOf(latitude);
            double y1 = Double.valueOf(longitude);
            int distanceFromCity = calculateDistance(x2, x1, y2, y1);
            feature.getProperties().setDistance(distanceFromCity);
        }

        return orderByDistanceWithLimit10(featureCollection);
    }

    private URI createURIForAPI(String latitude, String longitude, String maxRatio) throws URISyntaxException {
        LocalDate todaydate = LocalDate.now();
        URIBuilder uriBuilder = new URIBuilder(EARTHQUAKE_API_URI);
        uriBuilder.addParameter("starttime", todaydate.minusMonths(1).toString());
        uriBuilder.addParameter("endtime", todaydate.toString());
        uriBuilder.addParameter("latitude", latitude);
        uriBuilder.addParameter("longitude", longitude);
        uriBuilder.addParameter("maxradius", maxRatio);
        return uriBuilder.build();
    }

    private int calculateDistance(double x2, double x1, double y2, double y1) {
        return (int) (pow(x2 - x1, 2) + pow(y2 - y1, 2));
    }

    private List<Feature> orderByDistanceWithLimit10(FeatureCollection featureCollection) {
        return featureCollection.getFeatures().stream()
                .sorted(Comparator.comparingInt(o -> o.getProperties().getDistance()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
