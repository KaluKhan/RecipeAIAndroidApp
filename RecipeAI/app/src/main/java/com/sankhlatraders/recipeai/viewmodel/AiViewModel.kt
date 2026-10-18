package com.sankhlatraders.recipeai.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sankhlatraders.recipeai.data.model.LocationData
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

sealed class AiUiState {
    object Idle : AiUiState()
    object Loading : AiUiState()
    data class Success(val recipes: List<Recipe>, val locationContext: String) : AiUiState()
    data class Error(val message: String) : AiUiState()
}

@HiltViewModel
class AiViewModel @Inject constructor(
    private val repository: RecipeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    private val _locationInput = MutableStateFlow("")
    val locationInput: StateFlow<String> = _locationInput.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _locationData = MutableStateFlow<LocationData?>(null)
    val locationData: StateFlow<LocationData?> = _locationData.asStateFlow()

    private val _isLocationLoading = MutableStateFlow(false)
    val isLocationLoading: StateFlow<Boolean> = _isLocationLoading.asStateFlow()

    fun onLocationInputChange(input: String) {
        _locationInput.value = input
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getSuggestions() {
        viewModelScope.launch {
            val location = _locationInput.value.ifBlank {
                _locationData.value?.let { "${it.cityName}, ${it.countryName}" } ?: "Global"
            }
            _uiState.value = AiUiState.Loading

            repository.getAISuggestedRecipes(location = location, searchQuery = _searchQuery.value)
                .fold(onSuccess = { recipes ->
                    if (recipes.isEmpty()
                    ) {
                        _uiState.value =
                            AiUiState.Error("No recipes returned, Please try a different query")
                    } else {
                        _uiState.value = AiUiState.Success(recipes, location)
                    }

                }, onFailure = {
                    _uiState.value = AiUiState.Error(
                        it.message
                            ?: "Something went wrong. Please check your API key and try again."
                    )
                })
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchGpsLocation() {
        viewModelScope.launch {
            _isLocationLoading.value = true
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null
                ).await()
                location?.let { loc ->
                    val geocoder = Geocoder(context, Locale.getDefault())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(loc.latitude, loc.longitude, 1) { address ->
                            address.firstOrNull()?.let { address ->
                                val city = address.locality ?: address.adminArea ?: ""
                                val country = address.countryName ?: ""
                                _locationData.value = LocationData(
                                    latitude = loc.latitude,
                                    longitude = loc.longitude,
                                    cityName = city,
                                    countryName = country
                                )
                                _locationInput.value =
                                    if (city.isNotEmpty()) "$city, $country" else country
                            }
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                        addresses?.firstOrNull()?.let { address ->
                            val city = address.locality ?: address.adminArea ?: ""
                            val country = address.countryName ?: ""
                            _locationData.value = LocationData(
                                latitude = loc.latitude,
                                longitude = loc.longitude,
                                cityName = city,
                                countryName = country
                            )
                            _locationInput.value =
                                if (city.isNotEmpty()) "$city, $country" else country
                        }
                    }

                }
            } catch (e: Exception) {
                Log.e("AiViewModel", "Error getting location: ${e.message}")
            } finally {
                _isLocationLoading.value = false
            }
        }
    }

    fun reset() {
        _uiState.value = AiUiState.Idle
    }
}
