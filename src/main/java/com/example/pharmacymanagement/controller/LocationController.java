package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.LocationDto;
import com.example.pharmacymanagement.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto createLocation(@RequestBody @Valid LocationDto locationDto) {
        return locationService.createLocation(locationDto);
    }

    @GetMapping
    public Page<LocationDto> getAllLocations(Pageable pageable) {
        return locationService.getAllLocations(pageable);
    }

    @GetMapping("/{id}")
    public LocationDto getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping("/type/{type}")
    public List<LocationDto> getLocationsByType(@PathVariable String type) {
        return locationService.getLocationsByType(type);
    }

    @GetMapping("/search")
    public LocationDto findLocation(@RequestParam String type, @RequestParam String identifier) {
        return locationService.findByTypeAndIdentifier(type, identifier);
    }
}
