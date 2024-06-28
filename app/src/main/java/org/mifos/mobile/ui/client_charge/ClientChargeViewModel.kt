package org.mifos.mobile.ui.client_charge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.ClientChargeRepository
import org.mifos.mobile.core.datastore.model.Charge
import javax.inject.Inject

@HiltViewModel
class ClientChargeViewModel @Inject constructor(private val clientChargeRepositoryImp: ClientChargeRepository) :
    ViewModel() {

    private val _clientChargeUiState = MutableStateFlow<ClientChargeUiState>(ClientChargeUiState.Loading)
    val clientChargeUiState: StateFlow<ClientChargeUiState> get() = _clientChargeUiState

    private val _clientId = MutableStateFlow<Long?>(null)
    private val clientId: StateFlow<Long?> get() = _clientId

    private val _chargeType = MutableStateFlow<org.mifos.mobile.core.model.enums.ChargeType?>(null)
    private val chargeType: StateFlow<org.mifos.mobile.core.model.enums.ChargeType?> get() = _chargeType

    fun initArgs(
        clientId: Long?,
        chargeType: org.mifos.mobile.core.model.enums.ChargeType
    ) {
        _clientId.value = clientId
        _chargeType.value = chargeType
        loadCharges()
    }

    fun loadCharges() {
        clientId.value?.let { clientId ->
            when (chargeType.value) {
                org.mifos.mobile.core.model.enums.ChargeType.CLIENT -> loadClientCharges(clientId)
                org.mifos.mobile.core.model.enums.ChargeType.SAVINGS -> loadSavingsAccountCharges(clientId)
                org.mifos.mobile.core.model.enums.ChargeType.LOAN -> loadLoanAccountCharges(clientId)
                null -> Unit
            }
        }
    }

    private fun loadClientCharges(clientId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getClientCharges(clientId).catch {
                _clientChargeUiState.value = ClientChargeUiState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.Success(it.pageItems)
            }
        }
    }

    private fun loadLoanAccountCharges(loanId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getLoanCharges(loanId).catch {
                _clientChargeUiState.value = ClientChargeUiState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.Success(it)
            }
        }
    }

    private fun loadSavingsAccountCharges(savingsId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getSavingsCharges(savingsId).catch {
                _clientChargeUiState.value = ClientChargeUiState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.Success(it)
            }
        }
    }

    fun loadClientLocalCharges() {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.clientLocalCharges().catch {
                _clientChargeUiState.value = ClientChargeUiState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.Success(it.pageItems.filterNotNull())
            }
        }
    }
}

sealed class ClientChargeUiState {
    data object Loading : ClientChargeUiState()
    data class Error(val message: String?) : ClientChargeUiState()
    data class Success(val charges: List<Charge>) : ClientChargeUiState()
}
