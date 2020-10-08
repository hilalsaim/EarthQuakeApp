package com.example.nearbyearthquakes.controller;

import com.example.nearbyearthquakes.service.EarthquakeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;

@Controller
public class EarthquakeController {

    @Autowired
    EarthquakeService earthquakeService;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/search")
    public String searchEarthQuake(@RequestParam String latitude, @RequestParam String longitude, Model model)
            throws JsonProcessingException, URISyntaxException {
        model.addAttribute("results",
                earthquakeService.getEarthquakeList(latitude,longitude, "180"));
        return "search";
    }
}
