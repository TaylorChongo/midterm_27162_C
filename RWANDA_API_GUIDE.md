# Rwanda Administrative Structure API

This document describes the REST API implementation for Rwanda's administrative hierarchy:
**Province → District → Sector → Cell → Village**

## Overview

The system automatically initializes with Rwanda's administrative structure on startup. The hierarchical relationships allow you to:
- Create users (customers/pharmacists) using only a Village identifier
- Automatically link users to Cell, Sector, District, and Province through relationships
- Query users by any administrative level (Province, District, etc.)

## Administrative Structure

```
Rwanda (Country)
├── Kigali City (Province)
│   ├── Gasabo (District)
│   │   ├── Remera (Sector)
│   │   │   ├── Rukiri (Cell)
│   │   │   │   ├── Amahoro (Village)
│   │   │   │   └── Ubumwe (Village)
│   │   │   └── Nyabisindu (Cell)
│   │   │       ├── Kanyinya (Village)
│   │   │       └── Gisimenti (Village)
│   │   └── Kimironko (Sector)
│   │       └── Kibagabaga (Cell)
│   │           ├── Nyagatovu (Village)
│   │           └── Kamatamu (Village)
│   ├── Kicukiro (District)
│   └── Nyarugenge (District)
├── Eastern Province
├── Northern Province
├── Southern Province
└── Western Province
```

## API Endpoints

### 1. Location Management

#### Get All Locations (Paginated)
```http
GET /locations?page=0&size=20&sort=name,asc
```

#### Get Location by ID
```http
GET /locations/{id}
```

#### Get Locations by Type
```http
GET /locations/type/VILLAGE
GET /locations/type/PROVINCE
GET /locations/type/DISTRICT
GET /locations/type/SECTOR
GET /locations/type/CELL
```

#### Search Location by Code or Name
```http
GET /locations/search?type=VILLAGE&identifier=AMH
GET /locations/search?type=VILLAGE&identifier=Amahoro
GET /locations/search?type=PROVINCE&identifier=KGL
```

#### Create New Location
```http
POST /locations
Content-Type: application/json

{
  "name": "New Village",
  "code": "NVL",
  "type": "VILLAGE",
  "parentLocationId": 15
}
```

### 2. Customer Management

#### Create Customer Using Village Identifier
You can create a customer using either the village code or name. The system will automatically link them to the complete hierarchy.

**Using Village Code:**
```http
POST /customers
Content-Type: application/json

{
  "name": "Jean Uwimana",
  "phone": "0788123456",
  "villageIdentifier": "AMH"
}
```

**Using Village Name:**
```http
POST /customers
Content-Type: application/json

{
  "name": "Marie Mukamana",
  "phone": "0788654321",
  "villageIdentifier": "Amahoro"
}
```

**Using Location ID (Alternative):**
```http
POST /customers
Content-Type: application/json

{
  "name": "Paul Habimana",
  "phone": "0788999888",
  "locationId": 15
}
```

#### Get All Customers (Paginated)
```http
GET /customers?page=0&size=20
```

#### Get Customers by Province
Retrieve all customers in a province using either province code or name:

```http
GET /customers/province?code=KGL
GET /customers/province?name=Kigali City
```

## Sample Village Codes

Here are some village codes you can use for testing:

| Village Code | Village Name | Cell | Sector | District | Province |
|--------------|--------------|------|--------|----------|----------|
| AMH | Amahoro | Rukiri | Remera | Gasabo | Kigali City |
| UBU | Ubumwe | Rukiri | Remera | Gasabo | Kigali City |
| KAN | Kanyinya | Nyabisindu | Remera | Gasabo | Kigali City |
| GIS | Gisimenti | Nyabisindu | Remera | Gasabo | Kigali City |
| NYG | Nyagatovu | Kibagabaga | Kimironko | Gasabo | Kigali City |
| KAM | Kamatamu | Kibagabaga | Kimironko | Gasabo | Kigali City |
| NYM | Nyamirambo | Gikondo I | Gikondo | Kicukiro | Kigali City |
| REB | Rebero | Gikondo I | Gikondo | Kicukiro | Kigali City |
| BIR | Biryogo | Rwampara | Nyarugenge | Nyarugenge | Kigali City |
| KAB | Kabare | Nyakariro | Fumbwe | Rwamagana | Eastern Province |
| BUS | Busogo | Cyuve | Muhoza | Musanze | Northern Province |
| MAT | Matyazo | Karama | Tumba | Huye | Southern Province |
| UMU | Umuganda | Rubavu | Gisenyi | Rubavu | Western Province |

## How It Works

### Hierarchical Relationships

When you create a customer with a village identifier:

1. The system finds the Village by code or name
2. The Village is linked to its Cell (parent)
3. The Cell is linked to its Sector (parent)
4. The Sector is linked to its District (parent)
5. The District is linked to its Province (parent)
6. The Province is linked to the Country (parent)

This means:
- **No need to specify all levels** - just provide the village
- **Automatic linking** - relationships are maintained through the database
- **Easy querying** - retrieve users by any administrative level

### Example Flow

```json
// 1. Create customer with village code
POST /customers
{
  "name": "Jean Uwimana",
  "phone": "0788123456",
  "villageIdentifier": "AMH"
}

// 2. System resolves:
// AMH (Village) → Rukiri (Cell) → Remera (Sector) → Gasabo (District) → Kigali City (Province)

// 3. Customer is saved with location_id pointing to Amahoro village

// 4. Query all customers in Kigali City province
GET /customers/province?code=KGL

// 5. Returns Jean Uwimana (and all other customers in any village within Kigali City)
```

## Database Schema

### locations table
```sql
CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    type VARCHAR(50) NOT NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES locations(id)
);
```

### customers table
```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);
```

## Benefits

1. **Data Integrity**: Hierarchical structure ensures valid administrative divisions
2. **Simplified Input**: Users only need to know their village
3. **Flexible Queries**: Retrieve users at any administrative level
4. **Scalability**: Easy to add new villages, cells, sectors, etc.
5. **Consistency**: Standardized location codes across the system

## Testing

Run the application and test with:

```bash
# Get all villages
curl http://localhost:8080/locations/type/VILLAGE

# Create a customer in Amahoro village
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "phone": "0788000000",
    "villageIdentifier": "AMH"
  }'

# Get all customers in Kigali City
curl http://localhost:8080/customers/province?code=KGL
```
