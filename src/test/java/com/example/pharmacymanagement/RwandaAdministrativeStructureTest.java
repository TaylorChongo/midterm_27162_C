package com.example.pharmacymanagement;

import com.example.pharmacymanagement.entity.*;
import com.example.pharmacymanagement.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RwandaAdministrativeStructureTest {

    @Autowired private LocationRepository locationRepository;
    @Autowired private CustomerRepository customerRepository;

    @Test
    void testRwandaHierarchy_provinceToVillage() {
        // Find a village
        Location village = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "AMH")
                .orElseThrow(() -> new AssertionError("Village Amahoro not found"));

        assertThat(village.getName()).isEqualTo("Amahoro");
        assertThat(village.getType()).isEqualTo(LocationType.VILLAGE);

        // Traverse up the hierarchy
        Location cell = village.getParentLocation();
        assertThat(cell).isNotNull();
        assertThat(cell.getType()).isEqualTo(LocationType.CELL);
        assertThat(cell.getName()).isEqualTo("Rukiri");

        Location sector = cell.getParentLocation();
        assertThat(sector).isNotNull();
        assertThat(sector.getType()).isEqualTo(LocationType.SECTOR);
        assertThat(sector.getName()).isEqualTo("Remera");

        Location district = sector.getParentLocation();
        assertThat(district).isNotNull();
        assertThat(district.getType()).isEqualTo(LocationType.DISTRICT);
        assertThat(district.getName()).isEqualTo("Gasabo");

        Location province = district.getParentLocation();
        assertThat(province).isNotNull();
        assertThat(province.getType()).isEqualTo(LocationType.PROVINCE);
        assertThat(province.getName()).isEqualTo("Kigali City");

        Location country = province.getParentLocation();
        assertThat(country).isNotNull();
        assertThat(country.getType()).isEqualTo(LocationType.COUNTRY);
        assertThat(country.getName()).isEqualTo("Rwanda");
    }

    @Test
    void testCustomerCreation_linkedToVillage() {
        // Find village by code
        Location village = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "AMH")
                .orElseThrow(() -> new AssertionError("Village not found"));

        // Create customer in this village
        Customer customer = new Customer();
        customer.setName("Jean Uwimana");
        customer.setPhone("0788123456");
        customer.setLocation(village);
        customer = customerRepository.save(customer);

        // Verify customer is linked to village
        assertThat(customer.getLocation().getName()).isEqualTo("Amahoro");
        assertThat(customer.getLocation().getType()).isEqualTo(LocationType.VILLAGE);

        // Verify we can traverse to province
        Location province = customer.getLocation()
                .getParentLocation()  // Cell
                .getParentLocation()  // Sector
                .getParentLocation()  // District
                .getParentLocation(); // Province

        assertThat(province.getName()).isEqualTo("Kigali City");
    }

    @Test
    void testQueryCustomersByProvince() {
        // Create customers in different villages within Kigali
        Location amahoro = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "AMH").orElseThrow();
        Location ubumwe = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "UBU").orElseThrow();
        Location kanyinya = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "KAN").orElseThrow();

        Customer c1 = new Customer();
        c1.setName("Customer 1");
        c1.setPhone("0781111111");
        c1.setLocation(amahoro);
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("Customer 2");
        c2.setPhone("0782222222");
        c2.setLocation(ubumwe);
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setName("Customer 3");
        c3.setPhone("0783333333");
        c3.setLocation(kanyinya);
        customerRepository.save(c3);

        // Query by province code
        List<Customer> kigaliCustomers = customerRepository.findByProvinceCodeOrName("KGL", "");
        assertThat(kigaliCustomers).hasSizeGreaterThanOrEqualTo(3);

        // Query by province name
        List<Customer> kigaliCustomers2 = customerRepository.findByProvinceCodeOrName("", "Kigali City");
        assertThat(kigaliCustomers2).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void testAllProvinces_exist() {
        List<Location> provinces = locationRepository.findAll().stream()
                .filter(l -> l.getType() == LocationType.PROVINCE)
                .toList();

        assertThat(provinces).hasSizeGreaterThanOrEqualTo(5);
        
        List<String> provinceNames = provinces.stream()
                .map(Location::getName)
                .toList();

        assertThat(provinceNames).contains(
                "Kigali City",
                "Eastern Province",
                "Northern Province",
                "Southern Province",
                "Western Province"
        );
    }

    @Test
    void testVillageSearch_byCodeAndName() {
        // Search by code
        Location byCode = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "AMH")
                .orElseThrow(() -> new AssertionError("Village not found by code"));
        assertThat(byCode.getName()).isEqualTo("Amahoro");

        // Search by name
        Location byName = locationRepository.findByTypeAndCodeOrName(LocationType.VILLAGE, "Amahoro")
                .orElseThrow(() -> new AssertionError("Village not found by name"));
        assertThat(byName.getCode()).isEqualTo("AMH");

        // Both should be the same entity
        assertThat(byCode.getId()).isEqualTo(byName.getId());
    }
}
