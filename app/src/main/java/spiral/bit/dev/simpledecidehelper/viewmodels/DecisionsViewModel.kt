package spiral.bit.dev.simpledecidehelper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.repo.DecisionRepository
import javax.inject.Inject

@HiltViewModel
class DecisionsViewModel @Inject constructor(
    private val decisionRepo: DecisionRepository
) : ViewModel() {

    private var _allDecisions = MutableLiveData<List<Decision>>()
    val allDecisions: LiveData<List<Decision>> get() = _allDecisions

    private var _allCompletedDecisions = MutableLiveData<List<CompletedDecision>>()
    val allCompletedDecisions: LiveData<List<CompletedDecision>> get() = _allCompletedDecisions

    fun insertDecision(decision: Decision) = viewModelScope.launch {
        decisionRepo.insertDecision(decision)
    }

    fun deleteDecision(decision: Decision) = viewModelScope.launch {
        decisionRepo.deleteDecision(decision)
    }

    fun updateDecision(decision: Decision) = viewModelScope.launch {
        decisionRepo.updateDecision(decision)
    }

    fun deleteAllCompletedDecisions() = viewModelScope.launch(Dispatchers.IO) {
        decisionRepo.deleteAllCompletedDecisions()
    }

    fun insertCompletedDecision(completedDecision: CompletedDecision) = viewModelScope.launch {
        decisionRepo.insertCompletedDecision(completedDecision)
    }

    fun deleteCompletedDecision(completedDecision: CompletedDecision) = viewModelScope.launch {
        decisionRepo.deleteCompletedDecision(completedDecision)
    }

    init {
        viewModelScope.launch {
            _allCompletedDecisions =
                decisionRepo.getAllCompletedDecisions() as MutableLiveData<List<CompletedDecision>>
            _allDecisions = decisionRepo.getAllDecisions() as MutableLiveData<List<Decision>>
        }
    }
}