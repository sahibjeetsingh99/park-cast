# ParkCast

A parking prediction app for moviegoers. ParkCast estimates how full a theatre's parking lot will be based on ticket sales, and sends push notifications to help you plan your arrival time.

<img width="307" height="682" alt="image" src="https://github.com/user-attachments/assets/1e187267-562a-443a-a3a0-0c2a1af928ae" />

## How It Works

1. Theatre ticket sales are tracked in the database as bookings
2. When you open the app, it fetches parking predictions for all upcoming showtimes
3. The backend estimates cars from tickets sold (using a 2.1 people/car average) and calculates lot occupancy
4. If a showtime is 90 minutes away and parking will be busy, a push notification is sent automatically

## Tech Stack

- **Backend** — Spring Boot (Kotlin), PostgreSQL, Spring JPA, Firebase Admin SDK
- **Android** — Jetpack Compose, Retrofit2, ViewModel, Firebase Messaging

## Project Structure

```
park-cast/
├── backend/          # Spring Boot API
└── app/              # Android app
```

## Backend Setup

### Prerequisites
- Java 17+
- PostgreSQL running locally
- Firebase project with a service account key

### 1. Database
Create the database:
```bash
psql -U postgres -c "CREATE DATABASE parkcast;"
```

### 2. Configuration
Copy the example config and fill in your values:
```bash
cp backend/src/main/resources/application.properties.example \
   backend/src/main/resources/application.properties
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parkcast
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

### 3. Firebase
Place your Firebase service account key at:
```
backend/src/main/resources/firebase-service-account.json
```
Download this from Firebase Console → Project Settings → Service Accounts → Generate new private key.

### 4. Run
```bash
./gradlew bootRun
```

The server starts on `http://localhost:8080`. On first run, the database is seeded with sample theatres, showtimes, and bookings.

## Android Setup

### Prerequisites
- Android Studio
- Emulator or physical device (must have Google Play Services)

### 1. Firebase
Place your `google-services.json` at `app/google-services.json`.  
Download from Firebase Console → Project Settings → Your Apps → Android app.

### 2. Run
Open the `app/` folder in Android Studio and run the app.  
On first launch it fetches a real FCM token and registers it with the backend.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/theatres` | List all theatres |
| GET | `/api/showtimes` | List all showtimes |
| GET | `/api/showtimes/{id}/parking` | Parking prediction for a showtime |
| GET | `/api/theatres/{id}/parking` | Predictions for all showtimes at a theatre |
| POST | `/api/users/fcm-token` | Register a device FCM token |
| POST | `/api/notify/test/{showtimeId}` | Manually trigger a parking notification |

## Parking Status Levels

| Status | Occupancy | Recommendation |
|--------|-----------|----------------|
| AVAILABLE | < 40% | Normal arrival time is fine |
| MODERATE | 40–69% | Arrive 15 mins early |
| BUSY | 70–89% | Arrive 30 mins early |
| FULL | 90%+ | Arrive 45+ mins early or take transit |

## Push Notifications

Notifications are sent automatically every 5 minutes for showtimes starting in ~90 minutes when parking status is BUSY or FULL.

<img width="310" height="685" alt="image" src="https://github.com/user-attachments/assets/0919c301-3628-4860-9a80-c5a799670623" />


To trigger a test notification manually:
```bash
curl -X POST http://localhost:8080/api/notify/test/1
```
Put the app in the background first — Firebase only auto-displays notifications when the app is not in the foreground.
