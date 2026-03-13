package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.CustomerDto;
import com.example.pharmacymanagement.entity.Customer;
import com.example.pharmacymanagement.entity.Location;
import com.example.pharmacymanagement.entity.LocationType;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.CustomerRepository;
import com.example.pharmacymanagement.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;

    public CustomerService(CustomerRepository customerRepository, LocationRepository locationRepository) {
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
    }

    public CustomerDto createCustomer(CustomerDto dto) {
        Location location;
        
        if (dto.getVillageIdentifier() != null && !dto.getVillageIdentifier().isEmpty()) {
            // Find village by code or name
            location = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, dto.getVillageIdentifier())
                    .orElseThrow(() -> new ResourceNotFoundException("Village not found: " + dto.getVillageIdentifier()));
        } else if (dto.getLocationId() != null) {
            location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        } else {
            throw new IllegalArgumentException("Either locationId or villageIdentifier must be provided");
        }

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setLocation(location);

        Customer saved = customerRepository.save(customer);
        return mapToDto(saved);
    }

    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::mapToDto);
    }

    /**
     * Retrieves all customers in a province by province code OR province name.
     * Either param may be null — the repository query handles the OR logic.
     */
    public List<CustomerDto> getCustomersByProvince(String provinceCode, String provinceName) {
        return customerRepository.findByProvinceCodeOrName(
                provinceCode != null ? provinceCode : "",
                provinceName != null ? provinceName : "")
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private CustomerDto mapToDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setPhone(c.getPhone());
        dto.setLocationId(c.getLocation().getId());
        return dto;
    }
}
