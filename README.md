# SmartPark Service - Parking Management System

A REST API service for intelligent parking management system for urban areas.

## Features

- **JWT Authentication** - Secure API access with token-based authentication
- **Parking Lot Management** - Register and view parking lots with real-time occupancy
- **Vehicle Registration** - Register vehicles with type and owner information
- **Check-In/Check-Out** - Vehicle parking sessions with automatic cost calculation
- **Occupancy Tracking** - Monitor parking lot availability and occupancy percentage
- **Auto-Checkout** - Automatic removal of vehicles parked longer than 15 minutes
- **H2 Database** - In-memory database with preloaded sample data
- **Instant Testing (Preloaded Data)** - Skip the setup; start testing Check-In/Check-Out immediately with pre-seeded vehicles and parking lots (see [Preloaded Sample Data](#preloaded-sample-data) below)

## Prerequisites

- Java 8 or higher (tested with Java 26)
- Maven 3.6+ (tested with Maven 3.9.16)

## Build

```bash
# Clean and build the project
mvn clean install
```

## Run

```bash
# Start the application
mvn spring-boot:run

# The application will start on http://localhost:8080/api
```

The application uses an in-memory H2 database that is automatically initialized with sample parking lots and vehicles.

### Access H2 Console (Optional)
- URL: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:smartparkdb`
- Username: `sa`
- Password: (leave empty)

## Test

### Run Unit Tests
```bash
mvn test
```

### Manual Testing

#### 1. **Authenticate and Get JWT Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use the returned token in the `Authorization: Bearer <token>` header for all subsequent requests.

#### 2. **Register a Parking Lot**
```bash
curl -X POST http://localhost:8080/api/parking-lot \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "lotId": "LOT-100",
    "location": "Beach Parking",
    "capacity": 60,
    "costPerMinute": 0.40
  }'
```

#### 3. **Register a Vehicle**
```bash
curl -X POST http://localhost:8080/api/vehicle \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "NEW-1111",
    "type": "Car",
    "ownerName": "John Jason"
  }'
```

#### 4. **Check-In Vehicle**
```bash
curl -X POST http://localhost:8080/api/parking-session/check-in \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "NEW-1111",
    "lotId": "LOT-100"
  }'
```

#### 5. **Check-Out Vehicle**
```bash
curl -X POST http://localhost:8080/api/parking-session/check-out \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "NEW-1111"
  }'
```

**Response:**
```json
{
  "entryTime": "2026-07-16T09:30:51.944911",
  "exitTime": "2026-07-16T09:30:55.8295238",
  "licensePlate": "NEW-1111",
  "lotId": "LOT-100",
  "parkedMinutes": 1,
  "parkingFee": 0.40,
  "sessionId": 1
}
```

#### 6. **View Parking Lot Occupancy**
```bash
curl -X GET http://localhost:8080/api/parking-lot/LOT-100/occupancy \
  -H "Authorization: Bearer <TOKEN>"
```

**Response:**
```json
{
  "availableSpaces": 59,
  "capacity": 60,
  "location": "Beach Parking",
  "lotId": "LOT-100",
  "occupiedSpaces": 1
}
```

#### 7. **View All Vehicles in Parking Lot**
```bash
curl -X GET http://localhost:8080/api/parking-lot/LOT-100/vehicles \
  -H "Authorization: Bearer <TOKEN>"
```

**Response:**
```json
{
  "location": "Beach Parking",
  "lotId": "LOT-100",
  "totalVehicles": 1,
  "vehicles": [
    {
      "entryTime": "2026-07-16 09:31:53",
      "licensePlate": "NEW-1113",
      "ownerName": "John Jason",
      "type": "CAR"
    }
  ]
}
```
## Preloaded Sample Data

These records can be used to test the Check-In, Check-Out, and Auto Check-Out APIs.

### Vehicles
* **`CAR-001`**
  * (Type: `CAR` | Owner: Jan Josh)
* **`MTO-101`**
  * (Type: `MOTORCYCLE` | Owner: Luffy D)
* **`TRK-201`**
  * (Type: `TRUCK` | Owner: Nami san)

### Parking Lots
* **`LOT-A01`** 
  * (North Small Entrance)
  * (Capacity: 10 spaces | Cost: 2.50/min)
* **`LOT-B01`**
  * (South Medium Entrance)
  * (Capacity: 5 spaces | Cost: 3.00/min)
* **`VIP-Z01`**
  * (VIP Parking Area)
  * (Capacity: 2 spaces | Cost: 5.00/min)

## Postman Collection

Import `SmartPark API.postman_collection.json` into Postman to test all endpoints with pre-configured requests.

### Steps to Import:
1. Open Postman
2. Click **Import** button
3. Select **SmartPark.postman_collection.json**
4. Collection will appear in the Collections sidebar
5. Set the `token` variable after login in one of the requests

## Technology Stack

- **Framework:** Spring Boot 4.1.0
- **Java:** Version 26
- **Database:** H2 (in-memory)
- **Authentication:** JWT (JJWT 0.12.7)
- **Security:** Spring Security
- **Build Tool:** Maven
- **ORM:** Spring Data JPA/Hibernate
- **Validation:** Jakarta Validation

## Key Features Implementation

### JWT Authentication
Static credentials are configured:
- Username: `admin`
- Password: `admin`

Token expires in 5 Miniues.

Only has a Login Feature for now.

### Parking Session Rules
1. ✅ Vehicles cannot park in a full lot
2. ✅ Available spaces are updated on check-in/out
3. ✅ A vehicle can only be parked in one lot at a time
4. ✅ Parking cost is calculated based on minutes parked
5. ✅ Vehicles parked longer than 15 minutes are auto-removed by scheduler

### Database
- Uses H2 in-memory database
- Automatically creates tables on startup
- Data persists during application runtime
- Resets on application restart

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Resource not found
- `409 Conflict` - Parking lot full, vehicle already checked in, duplicate registration
- `500 Internal Server Error` - Server errors
