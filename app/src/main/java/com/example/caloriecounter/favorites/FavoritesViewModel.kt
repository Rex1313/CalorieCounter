package com.example.caloriecounter.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.Favourite
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.models.EntryTypeModel
import com.example.caloriecounter.models.UIEntry
import com.example.caloriecounter.models.UIFavorite
import com.example.caloriecounter.utils.CalculationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesViewModel : BaseViewModel() {
    val favoritesLiveData = MutableLiveData<List<UIFavorite>>()
    val favoriteLiveData = MutableLiveData<UIFavorite>()


    val entryTypes = arrayListOf(
        EntryTypeModel().apply {
            type = EntryType.FOOD
        },
        EntryTypeModel().apply {
            type = EntryType.EXCERCISE
        }
    )

    init {

    }


    suspend fun getFavorites() = withContext(Dispatchers.IO) {
        val entries = repository.getAllFavouritesAlphabetical()
        withContext(Dispatchers.Main) {
            favoritesLiveData.value =
                entries?.map { UIFavorite(it.name, it.value.toString(), it.id, it.type) }
        }
    }

    suspend fun filterFavorites(query: String) = withContext(Dispatchers.IO) {
        val entries = repository.getFavoritesStartingWith(query)
        withContext(Dispatchers.Main) {
            favoritesLiveData.value =
                entries?.map { UIFavorite(it.name, it.value.toString(), it.id, it.type) }
        }
    }

    suspend fun refreshData() = getFavorites()

    suspend fun deleteFavouriteById(id: Int?) {
        repository.deleteFavouriteById(id)
    }

    suspend fun getFavouriteById(id: Int?) {
        withContext(Dispatchers.IO) {
            repository.getFavouriteById(id)?.let { favourite ->
                withContext(Dispatchers.Main) {
                    favoriteLiveData.value = UIFavorite(
                        favourite.name,
                        favourite.value.toString(),
                        favourite.id,
                        favourite.type
                    )
                }
            }

        }
    }

    suspend fun editFavouriteById(id: Int?, name: String, value: String, entryType: EntryType) {
        val favoriteValue =
            if (entryType == EntryType.EXCERCISE) -CalculationUtils.calculateValueFromInput(
                value
            ) else {
                CalculationUtils.calculateValueFromInput(value)
            }
        repository.editFavourite(Favourite(id, favoriteValue, name, entryType.toString()))
    }

    suspend fun addFavorite(name: String, value: String, entryType: EntryType) {
        val favoriteValue =
            if (entryType == EntryType.EXCERCISE) -CalculationUtils.calculateValueFromInput(
                value
            ) else {
                CalculationUtils.calculateValueFromInput(value)
            }
        repository.addFavourite(favoriteValue, name, entryType.toString())
    }

    fun getEntryTypePosition(type: String): Int {
        return entryTypes.indexOfFirst { it.type.toString() == type }
    }
}
