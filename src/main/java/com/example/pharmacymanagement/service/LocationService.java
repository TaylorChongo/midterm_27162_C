package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.LocationDto;
import com.example.pharmacymanagement.entity.Location;
import com.example.pharmacymanagement.entity.LocationType;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationDto createLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setName(locationDto.getName());
        location.setCode(locationDto.getCode());
        location.setType(LocationType.valueOf(locationDto.getType().toUpperCase()));

        if (locationDto.getParentLocationId() != null) {
            Location parent = locationRepository.findById(locationDto.getParentLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent location not found"));
            location.setParentLocation(parent);
        }

        Location savedLocation = locationRepository.save(location);
        return mapToDto(savedLocation);
    }

    public Page<LocationDto> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable).map(this::mapToDto);
    }

    public LocationDto getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        return mapToDto(location);
    }

    public List<LocationDto> getLocationsByType(String type) {
        LocationType locationType = LocationType.valueOf(type.toUpperCase());
        return locationRepository.findAll().stream()
                .filter(loc -> loc.getType() == locationType)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public LocationDto findByTypeAndIdentifier(String type, String identifier) {
        LocationType locationType = LocationType.valueOf(type.toUpperCase());
        Location location = locationRepository.findByTypeAndCodeOrName(locationType, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with identifier: " + identifier));
        return mapToDto(location);
    }

    private LocationDto mapToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setCode(location.getCode());
        dto.setType(location.getType().name());
        if (location.getParentLocation() != null) {
            dto.setParentLocationId(location.getParentLocation().getId());
        }
        return dto;
    }
}
