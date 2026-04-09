// android/app/src/main/java/com/parkcast/android/ui/ParkingScreen.kt
package com.parkcast.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parkcast.android.model.ParkingPrediction
import com.parkcast.android.model.Showtime
import com.parkcast.android.viewmodel.ParkingViewModel

@Composable
fun ParkingScreen(viewModel: ParkingViewModel = viewModel()) {

    val showtimes by viewModel.showtimes.collectAsState()
    val prediction by viewModel.prediction.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadShowtimes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "ParkCast",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Smart Parking Predictions",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        error?.let {
            Text(text = it, color = Color.Red)
        }

        // Showtimes List
        Text(
            text = "Select a Showtime",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(showtimes) { showtime ->
                ShowtimeCard(
                    showtime = showtime,
                    onClick = { viewModel.loadPrediction(showtime.id) }
                )
            }
        }

        // Prediction Result
        prediction?.let {
            Spacer(modifier = Modifier.height(24.dp))
            ParkingPredictionCard(prediction = it)
        }
    }
}

@Composable
fun ShowtimeCard(showtime: Showtime, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = showtime.movieName,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = showtime.theatre.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = showtime.startTime,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ParkingPredictionCard(prediction: ParkingPrediction) {

    val statusColor = when (prediction.status) {
        "FULL"      -> Color(0xFFE53935)  // red
        "BUSY"      -> Color(0xFFFB8C00)  // orange
        "MODERATE"  -> Color(0xFFFDD835)  // yellow
        else        -> Color(0xFF43A047)  // green
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Parking Prediction",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status badge
            Surface(
                color = statusColor,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = prediction.status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Occupancy bar
            Text(
                text = "Lot occupancy: ${prediction.occupancyPercent}%",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { prediction.occupancyPercent / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = statusColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats
            Text(
                text = "Tickets sold: ${prediction.ticketsSold}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Estimated cars: ${prediction.estimatedCars}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Lot capacity: ${prediction.lotCapacity} spots",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Recommendation
            Text(
                text = prediction.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}