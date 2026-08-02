package net.farman.graphqldemo.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.farman.graphqldemo.GetCountriesQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    modifier: Modifier = Modifier,
    viewModel: CountryListViewModel = viewModel(),
    onCountryClick: (name: String) -> Unit,
) {
    // Observe state inside Compose lifecycle safely
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Countries") }) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val currentStatus = state) {
                is CountryListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is CountryListUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = currentStatus.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchAllCountries() }) {
                            Text("Retry")
                        }
                    }
                }
                is CountryListUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(currentStatus.countries) { country ->
                            CountryRow(country = country, onClick = onCountryClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountryRow(
    country: GetCountriesQuery.Country,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(country.code) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Name : ${country.name} ${country.emoji}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Capital: ${country.capital ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Currency: ${country.currency ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Continent: ${country.continent?.name ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
