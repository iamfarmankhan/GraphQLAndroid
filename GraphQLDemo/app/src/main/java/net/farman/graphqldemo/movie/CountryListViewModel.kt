package net.farman.graphqldemo.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.farman.graphqldemo.GetCountriesQuery
import net.farman.graphqldemo.appoloclient.apolloClient
import net.farman.graphqldemo.type.CountryFilterInput
import net.farman.graphqldemo.type.StringQueryOperatorInput

sealed interface CountryListUiState {
    object Loading : CountryListUiState
    data class Success(val countries: List<GetCountriesQuery.Country>) : CountryListUiState
    data class Error(val message: String) : CountryListUiState
}

class CountryListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)
    val uiState: StateFlow<CountryListUiState> = _uiState.asStateFlow()

    init {
        //fetchCountries(searchCountryByContinentCode("EU")) // AS code of Asia continent
        /*EU code of Asia continent,
        * ^Eng --- Country name starts with --- example England
        * land$ --- country name ends with -- Example Ireland,Iceland
        * ma -- country name contains -- Example Germany,Oman, Bahamas
        * */
      // fetchCountries(searchCountryByContinentCodeAndCountryName("EU", "^Fin"))
      // fetchCountries(searchCountryByCurrencyListUses(listOf("USD", "EUR", "GBP"))) // fetch countries which currencies from that lis
        fetchCountries(GetCountriesQuery())
    }

    private fun fetchCountries(query: GetCountriesQuery) {
        viewModelScope.launch {
            _uiState.value = CountryListUiState.Loading
            try {
                // Execute the auto-generated Apollo query
                val response = apolloClient.query(query).execute()
                Log.d("Farman", "GraphQL Server Errors: ${response.data}")
                if (response.exception != null) {
                    _uiState.value =
                        CountryListUiState.Error("Network Exception: ${response.exception?.localizedMessage}")
                    println("Apollo Network Error Tracked: ${response.exception}")
                    return@launch
                }
                if (response.hasErrors()) {
                    Log.d("Farman", "GraphQL Server Errors: ${response.errors}")
                    _uiState.value = CountryListUiState.Error(
                        response.errors?.get(0)?.message ?: "Failed to fetch data."
                    )
                    return@launch
                }
                val countries = response.data?.countries
                if (countries != null) {
                    _uiState.value = CountryListUiState.Success(countries)
                } else {
                    _uiState.value = CountryListUiState.Error("No countries found")
                }
            } catch (e: Exception) {
                _uiState.value =
                    CountryListUiState.Error(e.localizedMessage ?: "Failed to fetch data.")
            }
        }
    }


    private fun searchCountryByContinentCode(continentCode: String): GetCountriesQuery {
        return GetCountriesQuery(
            filter = Optional.present(
                CountryFilterInput(
                    continent = Optional.present(
                        StringQueryOperatorInput(
                            eq = Optional.present(
                                continentCode
                            )
                        )
                    ),
                )
            )
        )
    }

    private fun searchCountryByContinentCodeAndCountryName(
        continentCode: String,
        countryNameQuery: String
    ): GetCountriesQuery {
        return GetCountriesQuery(
            filter = Optional.present(
                CountryFilterInput(
                    continent = Optional.present(
                        StringQueryOperatorInput(
                            eq = Optional.present(
                                continentCode
                            )
                        )
                    ),
                    name = Optional.present(
                        StringQueryOperatorInput(
                            regex = Optional.present(
                                countryNameQuery
                            )
                        )
                    )
                )
            )
        )
    }

    private fun searchCountryByCurrencyListUses(currencies : List<String>) : GetCountriesQuery{
        return GetCountriesQuery(
            filter = Optional.present(
                CountryFilterInput(
                    currency = Optional.present(
                        StringQueryOperatorInput(
                            `in` = Optional.present(currencies) // Finds countries using these currencies
                        )
                    )
                )
            )
        )
    }

    fun fetchAllCountries(){
        fetchCountries(GetCountriesQuery())
    }


}
