package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.entity.Location;
import com.example.pharmacymanagement.entity.LocationType;
import com.example.pharmacymanagement.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RwandaDataInitializer implements CommandLineRunner {

    private final LocationRepository locationRepository;

    public RwandaDataInitializer(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        if (locationRepository.count() > 0) {
            return; // Data already initialized
        }

        // Country
        Location rwanda = save("Rwanda", "RW", LocationType.COUNTRY, null);

        // Provinces
        Location kigali = save("Kigali City", "KGL", LocationType.PROVINCE, rwanda);
        Location eastern = save("Eastern Province", "EP", LocationType.PROVINCE, rwanda);
        Location northern = save("Northern Province", "NP", LocationType.PROVINCE, rwanda);
        Location southern = save("Southern Province", "SP", LocationType.PROVINCE, rwanda);
        Location western = save("Western Province", "WP", LocationType.PROVINCE, rwanda);

        // Kigali City Districts
        Location gasabo = save("Gasabo", "GAS", LocationType.DISTRICT, kigali);
        Location kicukiro = save("Kicukiro", "KIC", LocationType.DISTRICT, kigali);
        Location nyarugenge = save("Nyarugenge", "NYA", LocationType.DISTRICT, kigali);

        // Sample Sectors in Gasabo
        Location remera = save("Remera", "REM", LocationType.SECTOR, gasabo);
        Location kimironko = save("Kimironko", "KIM", LocationType.SECTOR, gasabo);

        // Sample Cells in Remera
        Location rukiri = save("Rukiri", "RUK", LocationType.CELL, remera);
        Location nyabisindu = save("Nyabisindu", "NYB", LocationType.CELL, remera);

        // Sample Villages in Rukiri
        save("Amahoro", "AMH", LocationType.VILLAGE, rukiri);
        save("Ubumwe", "UBU", LocationType.VILLAGE, rukiri);

        // Sample Villages in Nyabisindu
        save("Kanyinya", "KAN", LocationType.VILLAGE, nyabisindu);
        save("Gisimenti", "GIS", LocationType.VILLAGE, nyabisindu);

        // Sample Cells in Kimironko
        Location kibagabaga = save("Kibagabaga", "KIB", LocationType.CELL, kimironko);
        
        // Sample Villages in Kibagabaga
        save("Nyagatovu", "NYG", LocationType.VILLAGE, kibagabaga);
        save("Kamatamu", "KAM", LocationType.VILLAGE, kibagabaga);

        // Sample Sectors in Kicukiro
        Location gikondo = save("Gikondo", "GIK", LocationType.SECTOR, kicukiro);
        save("Niboye", "NIB", LocationType.SECTOR, kicukiro);

        // Sample Cells in Gikondo
        Location gikondo1 = save("Gikondo I", "GIK1", LocationType.CELL, gikondo);
        
        // Sample Villages in Gikondo I
        save("Nyamirambo", "NYM", LocationType.VILLAGE, gikondo1);
        save("Rebero", "REB", LocationType.VILLAGE, gikondo1);

        // Sample Sectors in Nyarugenge
        Location nyarugenge1 = save("Nyarugenge", "NYA1", LocationType.SECTOR, nyarugenge);
        save("Muhima", "MUH", LocationType.SECTOR, nyarugenge);

        // Sample Cells in Nyarugenge Sector
        Location rwampara = save("Rwampara", "RWA", LocationType.CELL, nyarugenge1);
        
        // Sample Villages in Rwampara
        save("Biryogo", "BIR", LocationType.VILLAGE, rwampara);
        save("Nyabugogo", "NYB2", LocationType.VILLAGE, rwampara);

        // Eastern Province - Sample District
        Location rwamagana = save("Rwamagana", "RWM", LocationType.DISTRICT, eastern);
        save("Kayonza", "KYZ", LocationType.DISTRICT, eastern);

        // Sample Sector in Rwamagana
        Location fumbwe = save("Fumbwe", "FUM", LocationType.SECTOR, rwamagana);
        
        // Sample Cell in Fumbwe
        Location nyakariro = save("Nyakariro", "NYK", LocationType.CELL, fumbwe);
        
        // Sample Villages in Nyakariro
        save("Kabare", "KAB", LocationType.VILLAGE, nyakariro);
        save("Rugarama", "RUG", LocationType.VILLAGE, nyakariro);

        // Northern Province - Sample District
        Location musanze = save("Musanze", "MUS", LocationType.DISTRICT, northern);
        
        // Sample Sector in Musanze
        Location muhoza = save("Muhoza", "MHZ", LocationType.SECTOR, musanze);
        
        // Sample Cell in Muhoza
        Location cyuve = save("Cyuve", "CYU", LocationType.CELL, muhoza);
        
        // Sample Villages in Cyuve
        save("Busogo", "BUS", LocationType.VILLAGE, cyuve);
        save("Kinigi", "KIN", LocationType.VILLAGE, cyuve);

        // Southern Province - Sample District
        Location huye = save("Huye", "HUY", LocationType.DISTRICT, southern);
        
        // Sample Sector in Huye
        Location tumba = save("Tumba", "TUM", LocationType.SECTOR, huye);
        
        // Sample Cell in Tumba
        Location karama = save("Karama", "KAR", LocationType.CELL, tumba);
        
        // Sample Villages in Karama
        save("Matyazo", "MAT", LocationType.VILLAGE, karama);
        save("Rango", "RAN", LocationType.VILLAGE, karama);

        // Western Province - Sample District
        Location rubavu = save("Rubavu", "RUB", LocationType.DISTRICT, western);
        
        // Sample Sector in Rubavu
        Location gisenyi = save("Gisenyi", "GIS2", LocationType.SECTOR, rubavu);
        
        // Sample Cell in Gisenyi
        Location rubavu1 = save("Rubavu", "RUB1", LocationType.CELL, gisenyi);
        
        // Sample Villages in Rubavu Cell
        save("Umuganda", "UMU", LocationType.VILLAGE, rubavu1);
        save("Pfunda", "PFU", LocationType.VILLAGE, rubavu1);
    }

    private Location save(String name, String code, LocationType type, Location parent) {
        Location location = new Location();
        location.setName(name);
        location.setCode(code);
        location.setType(type);
        location.setParentLocation(parent);
        return locationRepository.save(location);
    }
}
