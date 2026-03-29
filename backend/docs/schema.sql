CREATE TABLE theatres(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, --"Cineplex vaughan"
    address VARCHAR(255),
    lot_capacity INTEGER NOT NULL
); --500

CREATE TABLE showtimes(
    id SERIAL PRIMARY KEY,
    theatre_id INTEGER REFERENCES theatres(id),
    movie_name VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    total_seats INTEGER NOT NULL
)

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    fcm_token VARCHAR(255) --for push notification
    created_at TIMESTAMP DEFAULT NOW()
 )

 CREATE TABLE bookings(
    id SERIAL PRIMARY KEY,
    showtime_id INTEGER REFERENCES showtime(id),
    user_id INTEGER REFERENCES users(id),
    seats INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
 )