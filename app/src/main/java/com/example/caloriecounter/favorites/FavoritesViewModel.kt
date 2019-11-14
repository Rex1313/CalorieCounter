package com.example.caloriecounter.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.Favourite
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.models.EntryTypeModel
import com.example.caloriecounter.models.UIEntry
import com.example.caloriecounter.models.UIFavorite
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
        repository.editFavourite(Favourite(id, value.toFloat(), name, entryType.toString()))
    }

    fun getEntryTypePosition(type: String): Int {
        return entryTypes.indexOfFirst { it.type.toString() == type }
    }
}
