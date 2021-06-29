package spiral.bit.dev.simpledecidehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import spiral.bit.dev.simpledecidehelper.repo.ProsConsRepository
import javax.inject.Inject

@HiltViewModel
class ProsConsViewModel @Inject constructor(
    private val prosConsRepo: ProsConsRepository
) : ViewModel() {

    lateinit var allComplProsCons: LiveData<List<CompletedProsConsItem>>
    lateinit var allProsCons: LiveData<List<ProsConsItem>>

    fun insertProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        prosConsRepo.insertProsConsItem(prosConsItem)
    }

    fun deleteProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        prosConsRepo.deleteProsConsItem(prosConsItem)
    }

    fun deleteAllProsConsByID(parentID: Int) = viewModelScope.launch {
        prosConsRepo.deleteAllProsConsByID(parentID)
    }

    fun insertComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        viewModelScope.launch {
            prosConsRepo.insertComplProsConsItem(completedProsConsItem)
        }

    fun deleteComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        viewModelScope.launch {
            prosConsRepo.deleteComplProsConsItem(completedProsConsItem)
        }

    fun deleteAllComplProsConsByID(parentID: Int) = viewModelScope.launch {
        prosConsRepo.deleteAllComplProsConsByID(parentID)
    }

    fun setParentId(parentId: Int) {
        allProsCons = prosConsRepo.getAllProsConsItems(parentId)
        allComplProsCons = prosConsRepo.getAllComplProsConsItems(parentId)
    }
}