package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel

@HiltViewModel(assistedFactory = ConfirmInfoViewModel.Factory::class)
class ConfirmInfoViewModel @AssistedInject constructor(
    private val sessionManager: SessionManager,
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-ConfirmInfoViewModel"
    }

    var userSession by mutableStateOf<UserSession?>(null)
        private set

    init {
        viewModelScope.launch {
            userSession = sessionManager.userSession.first()
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): ConfirmInfoViewModel
    }
}