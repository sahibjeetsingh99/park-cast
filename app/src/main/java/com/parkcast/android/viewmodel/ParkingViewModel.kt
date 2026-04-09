// android/app/src/main/java/com/parkcast/android/viewmodel/ParkingViewModel.kt
package com.parkcast.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkcast.android.model.ParkingPrediction
import com.parkcast.android.model.Showtime
import com.parkcast.android.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParkingViewModel : ViewModel() {

    private val _showtimes = MutableStateFlow<List<Showtime>>(emptyList())
    val showtimes: StateFlow<List<Showtime>> = _showtimes

    private val _prediction = MutableStateFlow<ParkingPrediction?>(null)
    val prediction: StateFlow<ParkingPrediction?> = _prediction

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadShowtimes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _showtimes.value = RetrofitClient.api.getShowtimes()
            } catch (e: Exception) {
                _error.value = "Failed to load showtimes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPrediction(showtimeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _prediction.value = RetrofitClient.api.getParkingPrediction(showtimeId)
            } catch (e: Exception) {
                _error.value = "Failed to load prediction: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}